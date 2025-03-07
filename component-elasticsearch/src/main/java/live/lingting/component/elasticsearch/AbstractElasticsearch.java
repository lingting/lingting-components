package live.lingting.component.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ErrorCause;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse;
import co.elastic.clients.elasticsearch.core.ScrollRequest;
import co.elastic.clients.elasticsearch.core.ScrollResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateByQueryRequest;
import co.elastic.clients.elasticsearch.core.UpdateByQueryResponse;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TrackHits;
import co.elastic.clients.util.ObjectBuilder;
import live.lingting.component.core.function.ThrowingRunnable;
import live.lingting.component.core.function.ThrowingSupplier;
import live.lingting.component.core.page.PageLimitParams;
import live.lingting.component.core.page.PageLimitResult;
import live.lingting.component.core.page.PageScrollParams;
import live.lingting.component.core.page.PageScrollResult;
import live.lingting.component.core.retry.Retry;
import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.core.util.StringUtils;
import live.lingting.component.elasticsearch.builder.ScriptBuilder;
import live.lingting.component.elasticsearch.cursor.LimitCursor;
import live.lingting.component.elasticsearch.cursor.ScrollCursor;
import live.lingting.component.elasticsearch.datascope.DataPermissionHandler;
import live.lingting.component.elasticsearch.datascope.DataScope;
import live.lingting.component.elasticsearch.properties.ElasticsearchProperties;
import live.lingting.component.elasticsearch.retry.ElasticsearchRetry;
import live.lingting.component.elasticsearch.util.ElasticSearchUtils;
import live.lingting.component.elasticsearch.wrapper.Queries;
import live.lingting.component.elasticsearch.wrapper.SortWrapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * @author lingting 2023-06-06 16:12
 */
@SuppressWarnings("java:S6813")
public abstract class AbstractElasticsearch<T> {

	@Getter
	@Setter
	private static Time defaultScrollTime = Time.of(t -> t.time("15m"));

	@Getter
	@Setter
	private static long defaultScrollSize = 100L;

	protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	@Autowired
	protected ElasticsearchClient client;

	@Autowired
	protected DataPermissionHandler dataPermissionHandler;

	@Autowired
	protected ElasticsearchProperties properties;

	protected final String index = ElasticSearchUtils.index(cls());

	protected final Class<T> cls = cls();

	@SuppressWarnings("java:S1172")
	public String documentId(T t) {
		return null;
	}

	public Class<T> cls() {
		return ElasticSearchUtils.getEntityClass(getClass());
	}

	protected void retry(ThrowingRunnable runnable) throws Exception {
		retry(() -> {
			runnable.run();
			return null;
		});
	}

	protected <R> R retry(ThrowingSupplier<R> supplier) throws Exception {
		ElasticsearchProperties.Retry propertiesRetry = properties.getRetry() == null
				? new ElasticsearchProperties.Retry() : properties.getRetry();

		Retry<R> retry = new ElasticsearchRetry<>(propertiesRetry, supplier);
		return retry.get();
	}

	protected void tryCatch(ThrowingRunnable runnable) {
		tryCatch(() -> {
			runnable.run();
			return null;
		}, e -> null);
	}

	protected <R> R tryCatch(ThrowingSupplier<R> supplier, Function<Exception, R> onCatch) {
		try {
			return supplier.get();
		}
		catch (Exception e) {
			return onCatch.apply(e);
		}
	}

	protected Query.Builder mergeQuery(Query... queries) {
		return mergeQuery(Arrays.stream(queries).filter(Objects::nonNull).collect(Collectors.toList()));
	}

	protected List<Query> dataScopeQueries() {
		// 数据权限
		List<DataScope> scopes = dataPermissionHandler.filterDataScopes(index);
		// 是否需要设置权限
		if (dataPermissionHandler.ignorePermissionControl(scopes, index)) {
			return Collections.emptyList();
		}

		return scopes.stream()
			.map(scope -> scope.getQuery(index))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}

	protected Query.Builder mergeQuery(List<Query> queries) {
		queries.addAll(dataScopeQueries());

		Query.Builder qb = new Query.Builder();
		qb.bool(bq -> bq.must(queries));
		return qb;
	}

	protected T getByQuery(Query... queries) throws IOException {
		return getByQuery(builder -> builder, queries);
	}

	protected T getByQuery(UnaryOperator<SearchRequest.Builder> operator, Query... queries) throws IOException {
		return search(builder -> operator.apply(builder).size(1), queries).hits()
			.stream()
			.findFirst()
			.map(Hit::source)
			.orElse(null);
	}

	protected long count(Query... queries) throws IOException {
		HitsMetadata<T> metadata = search(builder -> builder.size(0), queries);
		TotalHits hits = metadata.total();
		return hits == null ? 0 : hits.value();
	}

	protected HitsMetadata<T> search(Query... queries) throws IOException {
		return search(builder -> builder, queries);
	}

	protected HitsMetadata<T> search(UnaryOperator<SearchRequest.Builder> operator, Query... queries)
			throws IOException {
		Query.Builder qb = mergeQuery(queries);

		SearchRequest.Builder builder = operator.apply(new SearchRequest.Builder()
			// 返回匹配的所有文档数量
			.trackTotalHits(TrackHits.of(th -> th.enabled(true)))

		);
		builder.index(index);
		builder.query(qb.build());

		SearchResponse<T> searchResponse = client.search(builder.build(), cls);
		return searchResponse.hits();
	}

	protected List<SortOptions> ofLimitSort(Collection<PageLimitParams.Sort> sorts) {
		if (CollectionUtils.isEmpty(sorts)) {
			return new ArrayList<>();
		}
		return sorts.stream().map(sort -> {
			String field = StringUtils.underscoreToHump(sort.getField());
			return SortWrapper.sort(field, sort.getDesc());
		}).collect(Collectors.toList());
	}

	protected PageLimitResult<T> page(PageLimitParams params, Query... queries) throws IOException {
		List<SortOptions> sorts = ofLimitSort(params.getSorts());

		int from = (int) params.start();
		int size = params.getSize().intValue();

		HitsMetadata<T> hitsMetadata = search(builder -> builder.size(size).from(from).sort(sorts), queries);

		List<T> list = hitsMetadata.hits().stream().map(Hit::source).collect(Collectors.toList());
		long total = Optional.ofNullable(hitsMetadata.total()).map(TotalHits::value).orElse(0L);

		return new PageLimitResult<>(list, total);
	}

	protected void aggregations(BiConsumer<String, Aggregate> consumer, Map<String, Aggregation> aggregationMap,
			Query... queries) throws IOException {
		aggregations(builder -> builder, consumer, aggregationMap, queries);
	}

	protected void aggregations(UnaryOperator<SearchRequest.Builder> operator, BiConsumer<String, Aggregate> consumer,
			Map<String, Aggregation> aggregationMap, Query... queries) throws IOException {
		aggregations(operator, response -> {
			Map<String, Aggregate> aggregations = response.aggregations();
			Set<Map.Entry<String, Aggregate>> entries = aggregations.entrySet();
			for (Map.Entry<String, Aggregate> entry : entries) {
				String key = entry.getKey();
				Aggregate aggregate = entry.getValue();
				consumer.accept(key, aggregate);
			}
		}, aggregationMap, queries);
	}

	protected void aggregations(UnaryOperator<SearchRequest.Builder> operator, Consumer<SearchResponse<T>> consumer,
			Map<String, Aggregation> aggregationMap, Query... queries) throws IOException {

		Query.Builder qb = mergeQuery(queries);

		SearchRequest.Builder builder = operator.apply(new SearchRequest.Builder()
			// 返回匹配的所有文档数量
			.trackTotalHits(TrackHits.of(th -> th.enabled(true)))

		);
		builder.size(0);
		builder.index(index);
		builder.query(qb.build());
		builder.aggregations(aggregationMap);

		SearchResponse<T> response = client.search(builder.build(), cls);
		consumer.accept(response);
	}

	protected boolean update(String documentId, Function<Script.Builder, ObjectBuilder<Script>> scriptOperator)
			throws IOException {
		return update(documentId, scriptOperator.apply(new Script.Builder()).build());
	}

	protected boolean update(String documentId, Script script) throws IOException {
		return update(builder -> builder, documentId, script);
	}

	protected boolean update(UnaryOperator<UpdateRequest.Builder<T, T>> operator, String documentId, Script script)
			throws IOException {
		return update(builder -> operator.apply(builder).script(script), documentId);
	}

	protected boolean update(T t) throws IOException {
		return update(builder -> builder.doc(t), documentId(t));
	}

	protected boolean upsert(T doc) throws IOException {
		return update(builder -> builder.doc(doc).docAsUpsert(true), documentId(doc));
	}

	protected boolean upsert(T doc, Script script) throws IOException {
		return update(builder -> builder.doc(doc).script(script), documentId(doc));
	}

	protected boolean update(UnaryOperator<UpdateRequest.Builder<T, T>> operator, String documentId)
			throws IOException {
		UpdateRequest.Builder<T, T> builder = operator.apply(new UpdateRequest.Builder<T, T>()
			// 刷新策略
			.refresh(Refresh.WaitFor)
			// 版本冲突时自动重试次数
			.retryOnConflict(5));

		builder.index(index).id(documentId);

		UpdateResponse<T> response = client.update(builder.build(), cls);
		Result result = response.result();
		return Result.Updated.equals(result);
	}

	protected boolean updateByQuery(Function<Script.Builder, ObjectBuilder<Script>> scriptOperator, Query... queries)
			throws IOException {
		return updateByQuery(scriptOperator.apply(new Script.Builder()).build(), queries);
	}

	protected boolean updateByQuery(Script script, Query... queries) throws IOException {
		return updateByQuery(builder -> builder, script, queries);
	}

	protected boolean updateByQuery(UnaryOperator<UpdateByQueryRequest.Builder> operator, Script script,
			Query... queries) throws IOException {
		Query.Builder qb = mergeQuery(queries);

		UpdateByQueryRequest.Builder builder = operator.apply(new UpdateByQueryRequest.Builder()
			// 刷新策略
			.refresh(false));
		builder.index(index).query(qb.build()).script(script);

		UpdateByQueryResponse response = client.updateByQuery(builder.build());
		Long updated = response.updated();
		return updated != null && updated > 0;
	}

	protected BulkResponse bulk(BulkOperation... operations) throws IOException {
		return bulk(Arrays.stream(operations).collect(Collectors.toList()));
	}

	protected BulkResponse bulk(List<BulkOperation> operations) throws IOException {
		return bulk(builder -> builder, operations);
	}

	protected BulkResponse bulk(UnaryOperator<BulkRequest.Builder> operator, List<BulkOperation> operations)
			throws IOException {
		BulkRequest.Builder builder = operator.apply(new BulkRequest.Builder().refresh(Refresh.WaitFor));
		builder.index(index);
		builder.operations(operations);
		return client.bulk(builder.build());
	}

	protected void save(T t) throws IOException {
		saveBatch(Collections.singleton(t));
	}

	protected void saveBatch(Collection<T> collection) throws IOException {
		saveBatch(builder -> builder, collection);
	}

	protected void saveBatch(UnaryOperator<BulkRequest.Builder> operator, Collection<T> collection) throws IOException {
		if (CollectionUtils.isEmpty(collection)) {
			return;
		}

		List<BulkOperation> operations = new ArrayList<>();

		for (T t : collection) {
			String documentId = documentId(t);

			BulkOperation.Builder ob = new BulkOperation.Builder();
			ob.create(create -> create.id(documentId).document(t));
			operations.add(ob.build());
		}

		BulkResponse response = bulk(builder -> operator.apply(builder.refresh(Refresh.WaitFor)), operations);
		if (response.errors()) {
			List<BulkResponseItem> collect = response.items()
				.stream()
				.filter(item -> item.error() != null)
				.collect(Collectors.toList());
			BulkResponseItem first = collect.get(0);

			for (int i = 1; i < collect.size(); i++) {
				ErrorCause error = collect.get(i).error();
				log.warn("save error: {}", error);
			}

			// 全部保存失败, 抛异常
			if (collect.size() == collection.size()) {
				throw new IOException("bulk save error! " + first.error());
			}
			log.warn("save error: {}", first.error());
		}
	}

	protected boolean deleteByQuery(Query... queries) throws IOException {
		return deleteByQuery(builder -> builder, queries);
	}

	protected boolean deleteByQuery(UnaryOperator<DeleteByQueryRequest.Builder> operator, Query... queries)
			throws IOException {
		Query.Builder qb = mergeQuery(queries);

		DeleteByQueryRequest.Builder builder = operator.apply(new DeleteByQueryRequest.Builder().refresh(false));
		builder.index(index);
		builder.query(qb.build());

		DeleteByQueryResponse response = client.deleteByQuery(builder.build());
		Long deleted = response.deleted();
		return deleted != null && deleted > 0;
	}

	protected List<T> list(Query... queries) throws IOException {
		return list(builder -> builder, queries);
	}

	protected List<T> list(UnaryOperator<SearchRequest.Builder> operator, Query... queries) throws IOException {
		List<T> list = new ArrayList<>();

		PageScrollParams params = new PageScrollParams(getDefaultScrollSize(), null);
		List<T> records;

		do {
			PageScrollResult<T> result = scroll(operator, params, queries);
			records = result.getRecords();
			params.setCursor(result.getCursor());

			if (!CollectionUtils.isEmpty(records)) {
				list.addAll(records);
			}

		}
		while (!CollectionUtils.isEmpty(records) && params.getCursor() != null);

		return list;
	}

	protected PageScrollResult<T> scroll(PageScrollParams params, Query... queries) throws IOException {
		return scroll(builder -> builder, params, queries);
	}

	protected PageScrollResult<T> scroll(UnaryOperator<SearchRequest.Builder> operator, PageScrollParams params,
			Query... queries) throws IOException {
		String scrollId = null;
		if (params.getCursor() != null) {
			scrollId = params.getCursor().toString();
		}
		// 非首次滚动查询, 直接使用 scrollId
		if (StringUtils.hasText(scrollId)) {
			return scroll(builder -> builder.scroll(getDefaultScrollTime()), scrollId);
		}

		Query.Builder queryBuilder = mergeQuery(queries);
		SearchRequest.Builder builder = operator.apply(new SearchRequest.Builder().scroll(getDefaultScrollTime())
			// 返回匹配的所有文档数量
			.trackTotalHits(TrackHits.of(th -> th.enabled(true))))
			.index(index)
			.size(params.getSize().intValue())
			.query(queryBuilder.build());

		SearchResponse<T> search = client.search(builder.build(), cls);
		List<T> collect = search.hits()
			.hits()
			.stream()
			.map(Hit::source)
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		String nextScrollId = search.scrollId();

		// 如果首次滚动查询结果为空, 直接清除滚动上下文
		if (CollectionUtils.isEmpty(collect)) {
			clearScroll(nextScrollId);
		}

		return PageScrollResult.of(collect, nextScrollId);
	}

	protected PageScrollResult<T> scroll(UnaryOperator<ScrollRequest.Builder> operator, String scrollId)
			throws IOException {
		ScrollRequest.Builder builder = operator.apply(new ScrollRequest.Builder()).scrollId(scrollId);

		ScrollResponse<T> response = client.scroll(builder.build(), cls);
		List<T> collect = response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
		String nextScrollId = response.scrollId();

		if (CollectionUtils.isEmpty(collect)) {
			clearScroll(nextScrollId);
			return PageScrollResult.empty();
		}
		return PageScrollResult.of(collect, nextScrollId);
	}

	protected void clearScroll(String scrollId) throws IOException {
		if (!StringUtils.hasText(scrollId)) {
			return;
		}
		client.clearScroll(scr -> scr.scrollId(scrollId));
	}

	// region 游标方法

	protected LimitCursor<T> pageCursor(PageLimitParams params, Query... queries) {
		return new LimitCursor<>(page -> {
			params.setPage(page);
			return page(params, queries);
		});
	}

	protected ScrollCursor<T> scrollCursor(PageScrollParams params, Query... queries) throws IOException {
		PageScrollResult<T> scroll = scroll(params, queries);
		return new ScrollCursor<>(scrollId -> {
			params.setCursor(scrollId);
			return scroll(params, queries);
		}, scroll.getCursor().toString(), scroll.getRecords());
	}

	// endregion

	// region tools

	public Queries<T> queries() {
		return new Queries<>();
	}

	public ScriptBuilder<T> scriptBuilder() {
		return new ScriptBuilder<>();
	}

	// endregion

}
