package live.lingting.component.web.argumentresolve;

import cn.hutool.core.text.CharSequenceUtil;
import live.lingting.component.core.page.PageLimitParams;
import live.lingting.component.core.util.ArrayUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.ValidationAnnotationUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lingting 2022/10/25 11:37
 */
@Slf4j
@SuppressWarnings({ "java:S3740" })
public class PageLimitArgumentResolve implements HandlerMethodArgumentResolver {

	@Setter
	@Getter
	private String pageParameterName = PageLimitParams.DEFAULT_PAGE_PARAMETER;

	@Setter
	@Getter
	private String sizeParameterName = PageLimitParams.DEFAULT_SIZE_PARAMETER;

	@Setter
	@Getter
	private String sortParameterName = PageLimitParams.DEFAULT_SORT_PARAMETER;

	@Setter
	@Getter
	private int maxPageSize = PageLimitParams.DEFAULT_MAX_PAGE_SIZE;

	protected PageLimitParams getPageParam(MethodParameter parameter, HttpServletRequest request) {
		String pageParameterValue = request.getParameter(pageParameterName);
		String sizeParameterValue = request.getParameter(sizeParameterName);

		PageLimitParams pageParam;
		try {
			pageParam = (PageLimitParams) parameter.getParameterType().getDeclaredConstructor().newInstance();
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			pageParam = new PageLimitParams();
		}

		long pageValue = parseValueFormString(pageParameterValue, 1);
		pageParam.setPage(pageValue);

		long sizeValue = parseValueFormString(sizeParameterValue, 10);
		pageParam.setSize(sizeValue);

		// ========== 排序处理 ===========
		Map<String, String[]> parameterMap = request.getParameterMap();
		// sort 可以传多个，所以同时支持 sort 和 sort[]
		String[] sort = parameterMap.get(sortParameterName);
		if (ArrayUtils.isEmpty(sort)) {
			sort = parameterMap.get(sortParameterName + "[]");
		}

		List<PageLimitParams.Sort> sorts = new ArrayList<>();
		if (!ArrayUtils.isEmpty(sort)) {
			sorts = getSortList(sort);
		}
		pageParam.setSorts(sorts);
		return pageParam;
	}

	private long parseValueFormString(String currentParameterValue, long defaultValue) {
		try {
			return Long.parseLong(currentParameterValue);
		}
		catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * 封装排序规则
	 * @param sort 排序规则字符串
	 * @return List<PageParams.Sort>
	 */
	protected List<PageLimitParams.Sort> getSortList(String[] sort) {
		List<PageLimitParams.Sort> sorts = new ArrayList<>();

		// 将排序规则转换为 Sort 对象
		for (String sortRule : sort) {
			if (sortRule == null) {
				continue;
			}

			// 切割后最多两位，第一位 field 第二位 order
			String[] sortRuleArr = sortRule.split(",");

			// 字段
			String field = sortRuleArr[0];

			// 排序规则，默认正序
			String order;
			if (sortRuleArr.length < 2) {
				order = PageLimitParams.ASC;
			}
			else {
				order = sortRuleArr[1];
			}

			fillValidSort(field, order, sorts);
		}

		return sorts;
	}

	/**
	 * 校验并填充有效的 sort 对象到指定集合忠
	 * @param field 排序列
	 * @param order 排序顺序
	 * @param sorts sorts 集合
	 */
	protected void fillValidSort(String field, String order, List<PageLimitParams.Sort> sorts) {
		if (validFieldName(field)) {
			PageLimitParams.Sort sort = new PageLimitParams.Sort();
			// 驼峰转下划线
			sort.setDesc(!PageLimitParams.ASC.equalsIgnoreCase(order));
			// 正序/倒序
			sort.setField(CharSequenceUtil.toUnderlineCase(field));
			sorts.add(sort);
		}
	}

	/**
	 * 判断排序字段名是否非法 字段名只允许数字字母下划线，且不能是 sql 关键字
	 * @param filedName 字段名
	 * @return 是否非法
	 */
	protected boolean validFieldName(String filedName) {
		boolean isValid = CharSequenceUtil.isNotBlank(filedName) && filedName.matches(PageLimitParams.SORT_FILED_REGEX)
				&& !PageLimitParams.SQL_KEYWORDS.contains(filedName);
		if (!isValid) {
			log.warn("异常的分页查询排序字段：{}", filedName);
		}
		return isValid;
	}

	protected void paramValidate(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory, PageLimitParams pageParam)
			throws Exception {
		// 数据校验处理
		if (binderFactory != null) {
			WebDataBinder binder = binderFactory.createBinder(webRequest, pageParam, "pageParam");
			validateIfApplicable(binder, parameter);
			BindingResult bindingResult = binder.getBindingResult();

			long size = pageParam.getSize();
			if (size > maxPageSize) {
				bindingResult.addError(new ObjectError("size", "分页条数不能大于" + maxPageSize));
			}

			if (bindingResult.hasErrors() && isBindExceptionRequired(parameter)) {
				throw new MethodArgumentNotValidException(parameter, bindingResult);
			}
			if (mavContainer != null) {
				mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + "pageParam", bindingResult);
			}
		}
	}

	protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation ann : annotations) {
			Object[] validationHints = ValidationAnnotationUtils.determineValidationHints(ann);
			if (validationHints != null) {
				binder.validate(validationHints);
				break;
			}
		}
	}

	protected boolean isBindExceptionRequired(MethodParameter parameter) {
		int i = parameter.getParameterIndex();
		Class<?>[] paramTypes = parameter.getExecutable().getParameterTypes();
		boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
		return !hasBindingResult;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return PageLimitParams.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		if (request == null) {
			return new PageLimitParams();
		}

		PageLimitParams pageParam = getPageParam(parameter, request);

		paramValidate(parameter, mavContainer, webRequest, binderFactory, pageParam);

		return pageParam;
	}

}
