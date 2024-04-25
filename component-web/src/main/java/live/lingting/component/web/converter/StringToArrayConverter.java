/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package live.lingting.component.web.converter;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Set;

/**
 * Converts a comma-delimited String to an Array. Only matches if String.class can be
 * converted to the target array element type.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @since 3.0
 */
public class StringToArrayConverter implements ConverterByString<Object> {

	private final ConversionService conversionService;

	public StringToArrayConverter(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, Object[].class));
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return ConversionUtils.canConvertElements(sourceType, targetType.getElementTypeDescriptor(),
				this.conversionService);
	}

	Object array(TypeDescriptor type, int length) {
		TypeDescriptor target = type.getElementTypeDescriptor();
		Assert.state(target != null, "No target element type");
		return Array.newInstance(target.getType(), length);
	}

	@Override
	public Object nullValue(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return emptyValue(sourceType, targetType);
	}

	@Override
	public Object emptyValue(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return array(targetType, 0);
	}

	@Override
	public Object value(String source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		String[] fields = toArray(source);
		Object target = array(targetType, fields.length);
		TypeDescriptor descriptor = targetType.getElementTypeDescriptor();
		Assert.state(descriptor != null, "No target element descriptor!");
		for (int i = 0; i < fields.length; i++) {
			String sourceElement = fields[i];
			Object targetElement = this.conversionService.convert(sourceElement.trim(), sourceType, descriptor);
			Array.set(target, i, targetElement);
		}
		return target;
	}

}
