package live.lingting.component.aliyun.constant;

import lombok.experimental.UtilityClass;

/**
 * copy by <a href=
 * "https://help.aliyun.com/zh/oss/user-guide/overview-22#concept-y5r-5rm-2gb">阿里云文档</a>
 * <p>
 * 更新时间: 2023-11-13
 * </p>
 *
 * @author lingting 2023-04-21 15:04
 */
@UtilityClass
public class AliConstants {

	public static final String DEFAULT_PROTOCOL = "https";

	public static final String EFFECT_ALLOW = "Allow";

	// region action oss

	/** 列举请求者拥有的所有Bucket。 */
	public static final String ACTION_OSS_LIST_BUCKETS = "oss:ListBuckets";

	/** 创建Bucket。 */
	public static final String ACTION_OSS_PUT_BUCKET = "oss:PutBucket";

	/** 列举Bucket中所有Object的信息。 */
	public static final String ACTION_OSS_LIST_OBJECTS = "oss:ListObjects";

	/** 查看Bucket相关信息。 */
	public static final String ACTION_OSS_GET_BUCKET_INFO = "oss:GetBucketInfo";

	/** 查看Bucket位置信息。 */
	public static final String ACTION_OSS_GET_BUCKET_LOCATION = "oss:GetBucketLocation";

	/** 设置指定Bucket的版本控制状态。 */
	public static final String ACTION_OSS_PUT_BUCKET_VERSIONING = "oss:PutBucketVersioning";

	/** 获取指定Bucket的版本控制状态。 */
	public static final String ACTION_OSS_GET_BUCKET_VERSIONING = "oss:GetBucketVersioning";

	/** 列出Bucket中包括删除标记（Delete Marker）在内的所有Object的版本信息。 */
	public static final String ACTION_OSS_LIST_OBJECT_VERSIONS = "oss:ListObjectVersions";

	/** 设置或修改Bucket ACL。 */
	public static final String ACTION_OSS_PUT_BUCKET_ACL = "oss:PutBucketAcl";

	/** 获取Bucket ACL。 */
	public static final String ACTION_OSS_GET_BUCKET_ACL = "oss:GetBucketAcl";

	/** 删除某个Bucket。 */
	public static final String ACTION_OSS_DELETE_BUCKET = "oss:DeleteBucket";

	/** 新建合规保留策略。 */
	public static final String ACTION_OSS_INITIATE_BUCKET_WORM = "oss:InitiateBucketWorm";

	/** 删除未锁定的合规保留策略。 */
	public static final String ACTION_OSS_ABORT_BUCKET_WORM = "oss:AbortBucketWorm";

	/** 锁定合规保留策略。 */
	public static final String ACTION_OSS_COMPLETE_BUCKET_WORM = "oss:CompleteBucketWorm";

	/** 延长已锁定的合规保留策略对应Bucket中Object的保留天数。 */
	public static final String ACTION_OSS_EXTEND_BUCKET_WORM = "oss:ExtendBucketWorm";

	/** 获取合规保留策略信息。 */
	public static final String ACTION_OSS_GET_BUCKET_WORM = "oss:GetBucketWorm";

	/** 开启Bucket日志转存功能。 */
	public static final String ACTION_OSS_PUT_BUCKET_LOGGING = "oss:PutBucketLogging";

	/** 查看Bucket日志转存配置。 */
	public static final String ACTION_OSS_GET_BUCKET_LOGGING = "oss:GetBucketLogging";

	/** 关闭Bucket日志转存功能。 */
	public static final String ACTION_OSS_DELETE_BUCKET_LOGGING = "oss:DeleteBucketLogging";

	/** 设置Bucket为静态网站托管模式并设置其跳转规则（RoutingRule）。 */
	public static final String ACTION_OSS_PUT_BUCKET_WEBSITE = "oss:PutBucketWebsite";

	/** 查看Bucket的静态网站托管状态以及跳转规则。 */
	public static final String ACTION_OSS_GET_BUCKET_WEBSITE = "oss:GetBucketWebsite";

	/** 关闭Bucket的静态网站托管模式以及跳转规则。 */
	public static final String ACTION_OSS_DELETE_BUCKET_WEBSITE = "oss:DeleteBucketWebsite";

	/** 设置Bucket的防盗链。 */
	public static final String ACTION_OSS_PUT_BUCKET_REFERER = "oss:PutBucketReferer";

	/** 查看Bucket的防盗链（Referer）相关配置。 */
	public static final String ACTION_OSS_GET_BUCKET_REFERER = "oss:GetBucketReferer";

	/** 设置Bucket的生命周期规则。 */
	public static final String ACTION_OSS_PUT_BUCKET_LIFECYCLE = "oss:PutBucketLifecycle";

	/** 查看Bucket的生命周期规则。 */
	public static final String ACTION_OSS_GET_BUCKET_LIFECYCLE = "oss:GetBucketLifecycle";

	/** 删除Bucket的生命周期规则。 */
	public static final String ACTION_OSS_DELETE_BUCKET_LIFECYCLE = "oss:DeleteBucketLifecycle";

	/** 设置Bucket传输加速。 */
	public static final String ACTION_OSS_PUT_BUCKET_TRANSFER_ACCELERATION = "oss:PutBucketTransferAcceleration";

	/** 查看Bucket的传输加速配置。 */
	public static final String ACTION_OSS_GET_BUCKET_TRANSFER_ACCELERATION = "oss:GetBucketTransferAcceleration";

	/**
	 * 列举所有执行中的Multipart Upload事件，即已经初始化但还未完成（Complete）或者还未中止（Abort）的Multipart Upload事件。
	 */
	public static final String ACTION_OSS_LIST_MULTIPART_UPLOADS = "oss:ListMultipartUploads";

	/** 设置指定Bucket的跨域资源共享CORS（Cross-Origin Resource Sharing）规则。 */
	public static final String ACTION_OSS_PUT_BUCKET_CORS = "oss:PutBucketCors";

	/** 获取指定Bucket当前的跨域资源共享CORS规则。 */
	public static final String ACTION_OSS_GET_BUCKET_CORS = "oss:GetBucketCors";

	/** 关闭指定Bucket对应的跨域资源共享CORS功能并清空所有规则。 */
	public static final String ACTION_OSS_DELETE_BUCKET_CORS = "oss:DeleteBucketCors";

	/** 设置指定Bucket的授权策略。 */
	public static final String ACTION_OSS_PUT_BUCKET_POLICY = "oss:PutBucketPolicy";

	/** 获取指定Bucket的授权策略。 */
	public static final String ACTION_OSS_GET_BUCKET_POLICY = "oss:GetBucketPolicy";

	/** 删除指定Bucket的授权策略。 */
	public static final String ACTION_OSS_DELETE_BUCKET_POLICY = "oss:DeleteBucketPolicy";

	/** 添加或修改指定Bucket的标签。 */
	public static final String ACTION_OSS_PUT_BUCKET_TAGGING = "oss:PutBucketTagging";

	/** 获取Bucket的标签。 */
	public static final String ACTION_OSS_GET_BUCKET_TAGGING = "oss:GetBucketTagging";

	/** 删除Bucket的标签。 */
	public static final String ACTION_OSS_DELETE_BUCKET_TAGGING = "oss:DeleteBucketTagging";

	/** 配置Bucket的加密规则。 */
	public static final String ACTION_OSS_PUT_BUCKET_ENCRYPTION = "oss:PutBucketEncryption";

	/** 获取Bucket的加密规则。 */
	public static final String ACTION_OSS_GET_BUCKET_ENCRYPTION = "oss:GetBucketEncryption";

	/** 删除Bucket的加密规则。 */
	public static final String ACTION_OSS_DELETE_BUCKET_ENCRYPTION = "oss:DeleteBucketEncryption";

	/** 设置请求者付费模式。 */
	public static final String ACTION_OSS_PUT_BUCKET_REQUEST_PAYMENT = "oss:PutBucketRequestPayment";

	/** 获取请求者付费模式配置信息。 */
	public static final String ACTION_OSS_GET_BUCKET_REQUEST_PAYMENT = "oss:GetBucketRequestPayment";

	/** 设置Bucket的数据复制规则。 */
	public static final String ACTION_OSS_PUT_BUCKET_REPLICATION = "oss:PutBucketReplication";

	/** 为已有的跨区域复制规则开启或关闭数据复制时间控制（RTC）功能。 */
	public static final String ACTION_OSS_PUT_BUCKET_R_T_C = "oss:PutBucketRTC";

	/** 获取Bucket已设置的数据复制规则。 */
	public static final String ACTION_OSS_GET_BUCKET_REPLICATION = "oss:GetBucketReplication";

	/** 停止Bucket的数据复制并删除Bucket的复制配置。 */
	public static final String ACTION_OSS_DELETE_BUCKET_REPLICATION = "oss:DeleteBucketReplication";

	/** 获取可复制到的目标Bucket的所在地域。 */
	public static final String ACTION_OSS_GET_BUCKET_REPLICATION_LOCATION = "oss:GetBucketReplicationLocation";

	/** 获取Bucket的数据复制进度。 */
	public static final String ACTION_OSS_GET_BUCKET_REPLICATION_PROGRESS = "oss:GetBucketReplicationProgress";

	/** 配置Bucket的清单（Inventory）规则。 */
	public static final String ACTION_OSS_PUT_BUCKET_INVENTORY = "oss:PutBucketInventory";

	/** 查看Bucket中指定的清单任务。 */
	public static final String ACTION_OSS_GET_BUCKET_INVENTORY = "oss:GetBucketInventory";

	/** 删除Bucket中指定的清单任务。 */
	public static final String ACTION_OSS_DELETE_BUCKET_INVENTORY = "oss:DeleteBucketInventory";

	/** 配置Bucket的访问跟踪状态。 */
	public static final String ACTION_OSS_PUT_BUCKET_ACCESS_MONITOR = "oss:PutBucketAccessMonitor";

	/** 获取Bucket的访问跟踪状态。 */
	public static final String ACTION_OSS_GET_BUCKET_ACCESS_MONITOR = "oss:GetBucketAccessMonitor";

	/** 开启Bucket的元数据管理功能。 */
	public static final String ACTION_OSS_OPEN_META_QUERY = "oss:OpenMetaQuery";

	/** 获取Bucket的元数据索引库信息。 */
	public static final String ACTION_OSS_GET_META_QUERY_STATUS = "oss:GetMetaQueryStatus";

	/** 查询满足指定条件的Object，并按照指定字段和排序方式列出Object信息。 */
	public static final String ACTION_OSS_DO_META_QUERY = "oss:DoMetaQuery";

	/** 关闭Bucket的元数据管理功能. */
	public static final String ACTION_OSS_CLOSE_META_QUERY = "oss:CloseMetaQuery";

	/** 创建高防OSS实例。 */
	public static final String ACTION_OSS_INIT_USER_ANTI_D_DOS_INFO = "oss:InitUserAntiDDosInfo";

	/** 更改高防OSS实例状态。 */
	public static final String ACTION_OSS_UPDATE_USER_ANTI_D_DOS_INFO = "oss:UpdateUserAntiDDosInfo";

	/** 查询指定账号下的高防OSS实例信息。 */
	public static final String ACTION_OSS_GET_USER_ANTI_D_DOS_INFO = "oss:GetUserAntiDDosInfo";

	/** 初始化Bucket防护。 */
	public static final String ACTION_OSS_INIT_BUCKET_ANTI_D_DOS_INFO = "oss:InitBucketAntiDDosInfo";

	/** 更新Bucket防护状态。 */
	public static final String ACTION_OSS_UPDATE_BUCKET_ANTI_D_DOS_INFO = "oss:UpdateBucketAntiDDosInfo";

	/** 获取Bucket防护信息列表。 */
	public static final String ACTION_OSS_LIST_BUCKET_ANTI_D_DOS_INFO = "oss:ListBucketAntiDDosInfo";

	/** 设置Bucket所属资源组。 */
	public static final String ACTION_OSS_PUT_BUCKET_RESOURCE_GROUP = "oss:PutBucketResourceGroup";

	/** 查询Bucket所属资源组ID。 */
	public static final String ACTION_OSS_GET_BUCKET_RESOURCE_GROUP = "oss:GetBucketResourceGroup";

	/** 创建域名所有权验证所需的CnameToken。 */
	public static final String ACTION_OSS_CREATE_CNAME_TOKEN = "oss:CreateCnameToken";

	/** 获取已创建的CnameToken。 */
	public static final String ACTION_OSS_GET_CNAME_TOKEN = "oss:GetCnameToken";

	/** 为Bucket绑定自定义域名。 */
	public static final String ACTION_OSS_PUT_CNAME = "oss:PutCname";

	/** 获取Bucket下绑定的所有的自定义域名（Cname）列表。 */
	public static final String ACTION_OSS_LIST_CNAME = "oss:ListCname";

	/** 删除Bucket已绑定的Cname。 */
	public static final String ACTION_OSS_DELETE_CNAME = "oss:DeleteCname";

	/** 设置图片样式。 */
	public static final String ACTION_OSS_PUT_STYLE = "oss:PutStyle";

	/** 获取图片样式。 */
	public static final String ACTION_OSS_GET_STYLE = "oss:GetStyle";

	/** 列举图片样式。 */
	public static final String ACTION_OSS_LIST_STYLE = "oss:ListStyle";

	/** 删除图片样式。 */
	public static final String ACTION_OSS_DELETE_STYLE = "oss:DeleteStyle";

	/** 创建Bucket存储冗余类型转换任务。 */
	public static final String ACTION_OSS_CREATE_BUCKET_DATA_REDUNDANCY_TRANSITION = "oss:CreateBucketDataRedundancyTransition";

	/** 查看Bucket存储冗余类型转换任务。 */
	public static final String ACTION_OSS_GET_BUCKET_DATA_REDUNDANCY_TRANSITION = "oss:GetBucketDataRedundancyTransition";

	/** 列举Bucket下所有的存储冗余类型转换任务。 */
	public static final String ACTION_OSS_LIST_BUCKET_DATA_REDUNDANCY_TRANSITION = "oss:ListBucketDataRedundancyTransition";

	/** 删除Bucket存储冗余类型转换任务。 */
	public static final String ACTION_OSS_DELETE_BUCKET_DATA_REDUNDANCY_TRANSITION = "oss:DeleteBucketDataRedundancyTransition";

	/** 为Bucket开启或关闭归档直读。 */
	public static final String ACTION_OSS_PUT_BUCKET_ARCHIVE_DIRECT_READ = "oss:PutBucketArchiveDirectRead";

	/** 查看Bucket是否开启归档直读。 */
	public static final String ACTION_OSS_GET_BUCKET_ARCHIVE_DIRECT_READ = "oss:GetBucketArchiveDirectRead";

	/** 创建接入点。 */
	public static final String ACTION_OSS_CREATE_ACCESS_POINT = "oss:CreateAccessPoint";

	/** 获取单个接入点信息。 */
	public static final String ACTION_OSS_GET_ACCESS_POINT = "oss:GetAccessPoint";

	/** 删除接入点。 */
	public static final String ACTION_OSS_DELETE_ACCESS_POINT = "oss:DeleteAccessPoint";

	/** 获取用户级别及Bucket级别的接入点信息。 */
	public static final String ACTION_OSS_LIST_ACCESS_POINTS = "oss:ListAccessPoints";

	/** 配置接入点策略。 */
	public static final String ACTION_OSS_PUT_ACCESS_POINT_POLICY = "oss:PutAccessPointPolicy";

	/** 获取接入点策略信息。 */
	public static final String ACTION_OSS_GET_ACCESS_POINT_POLICY = "oss:GetAccessPointPolicy";

	/** 删除接入点策略。 */
	public static final String ACTION_OSS_DELETE_ACCESS_POINT_POLICY = "oss:DeleteAccessPointPolicy";

	/** 为Bucket开启或关闭TLS版本设置。 */
	public static final String ACTION_OSS_PUT_BUCKET_HTTPS_CONFIG = "oss:PutBucketHttpsConfig";

	/** 查看Bucket的TLS版本设置。 */
	public static final String ACTION_OSS_GET_BUCKET_HTTPS_CONFIG = "oss:GetBucketHttpsConfig";

	/** 复制过程涉及的列举权限。即允许OSS先列举源Bucket的历史数据，再逐一对历史数据进行复制。 */
	public static final String ACTION_OSS_REPLICATE_LIST = "oss:ReplicateList";

	/** 创建对象FC接入点。 */
	public static final String ACTION_OSS_CREATE_ACCESS_POINT_FOR_OBJECT_PROCESS = "oss:CreateAccessPointForObjectProcess";

	/** 获取对象FC接入点基础信息。 */
	public static final String ACTION_OSS_GET_ACCESS_POINT_FOR_OBJECT_PROCESS = "oss:GetAccessPointForObjectProcess";

	/** 删除对象FC接入点。 */
	public static final String ACTION_OSS_DELETE_ACCESS_POINT_FOR_OBJECT_PROCESS = "oss:DeleteAccessPointForObjectProcess";

	/** 获取用户级别的对象FC接入点信息。 */
	public static final String ACTION_OSS_LIST_ACCESS_POINTS_FOR_OBJECT_PROCESS = "oss:ListAccessPointsForObjectProcess";

	/** 修改对象FC接入点配置。 */
	public static final String ACTION_OSS_PUT_ACCESS_POINT_CONFIG_FOR_OBJECT_PROCESS = "oss:PutAccessPointConfigForObjectProcess";

	/** 获取对象FC接入点配置信息。 */
	public static final String ACTION_OSS_GET_ACCESS_POINT_CONFIG_FOR_OBJECT_PROCESS = "oss:GetAccessPointConfigForObjectProcess";

	/** 为对象FC接入点配置权限策略。 */
	public static final String ACTION_OSS_PUT_ACCESS_POINT_POLICY_FOR_OBJECT_PROCESS = "oss:PutAccessPointPolicyForObjectProcess";

	/** 获取对象FC接入点的权限策略配置。 */
	public static final String ACTION_OSS_GET_ACCESS_POINT_POLICY_FOR_OBJECT_PROCESS = "oss:GetAccessPointPolicyForObjectProcess";

	/** 删除对象FC接入点的权限策略。 */
	public static final String ACTION_OSS_DELETE_ACCESS_POINT_POLICY_FOR_OBJECT_PROCESS = "oss:DeleteAccessPointPolicyForObjectProcess";

	/** 自定义返回数据和响应标头。 */
	public static final String ACTION_OSS_WRITE_GET_OBJECT_RESPONSE = "oss:WriteGetObjectResponse";

	/** 上传文件（Object）。 */
	public static final String ACTION_OSS_PUT_OBJECT = "oss:PutObject";

	/** 取消MultipartUpload事件并删除对应的Part数据。 */
	public static final String ACTION_OSS_ABORT_MULTIPART_UPLOAD = "oss:AbortMultipartUpload";

	/** 获取某个Object。 */
	public static final String ACTION_OSS_GET_OBJECT = "oss:GetObject";

	/** 删除某个Object。 */
	public static final String ACTION_OSS_DELETE_OBJECT = "oss:DeleteObject";

	/** 列举指定Upload ID所属的所有已经上传成功的Part。 */
	public static final String ACTION_OSS_LIST_PARTS = "oss:ListParts";

	/** 修改Bucket下某个Object的ACL。 */
	public static final String ACTION_OSS_PUT_OBJECT_ACL = "oss:PutObjectAcl";

	/** 获取Bucket下某个Object的ACL。 */
	public static final String ACTION_OSS_GET_OBJECT_ACL = "oss:GetObjectAcl";

	/** 解冻归档存储、冷归档存储或者深度冷归档存储类型的Object。 */
	public static final String ACTION_OSS_RESTORE_OBJECT = "oss:RestoreObject";

	/** 设置或更新Object的标签（Tagging）信息。 */
	public static final String ACTION_OSS_PUT_OBJECT_TAGGING = "oss:PutObjectTagging";

	/** 获取Object的标签信息。 */
	public static final String ACTION_OSS_GET_OBJECT_TAGGING = "oss:GetObjectTagging";

	/** 删除指定Object的标签信息。 */
	public static final String ACTION_OSS_DELETE_OBJECT_TAGGING = "oss:DeleteObjectTagging";

	/** 下载指定版本Object。 */
	public static final String ACTION_OSS_GET_OBJECT_VERSION = "oss:GetObjectVersion";

	/** 修改Bucket下指定版本Object的ACL。 */
	public static final String ACTION_OSS_PUT_OBJECT_VERSION_ACL = "oss:PutObjectVersionAcl";

	/** 获取Bucket下指定版本Object的ACL。 */
	public static final String ACTION_OSS_GET_OBJECT_VERSION_ACL = "oss:GetObjectVersionAcl";

	/** 解冻指定版本的归档存储、冷归档存储或者深度冷归档存储类型的Object。 */
	public static final String ACTION_OSS_RESTORE_OBJECT_VERSION = "oss:RestoreObjectVersion";

	/** 删除指定版本Object。 */
	public static final String ACTION_OSS_DELETE_OBJECT_VERSION = "oss:DeleteObjectVersion";

	/** 设置或更新指定版本Object的标签（Tagging）信息。 */
	public static final String ACTION_OSS_PUT_OBJECT_VERSION_TAGGING = "oss:PutObjectVersionTagging";

	/** 获取指定版本Object的标签信息。 */
	public static final String ACTION_OSS_GET_OBJECT_VERSION_TAGGING = "oss:GetObjectVersionTagging";

	/** 删除指定版本Object的标签信息。 */
	public static final String ACTION_OSS_DELETE_OBJECT_VERSION_TAGGING = "oss:DeleteObjectVersionTagging";

	/** 通过RTMP协议上传音视频数据前，必须先调用该接口创建一个LiveChannel。 */
	public static final String ACTION_OSS_PUT_LIVE_CHANNEL = "oss:PutLiveChannel";

	/** 列举指定的LiveChannel。 */
	public static final String ACTION_OSS_LIST_LIVE_CHANNEL = "oss:ListLiveChannel";

	/** 删除指定的LiveChannel。 */
	public static final String ACTION_OSS_DELETE_LIVE_CHANNEL = "oss:DeleteLiveChannel";

	/** 在启用（enabled）和禁用（disabled）两种状态之间进行切换。 */
	public static final String ACTION_OSS_PUT_LIVE_CHANNEL_STATUS = "oss:PutLiveChannelStatus";

	/** 获取指定LiveChannel的配置信息。 */
	public static final String ACTION_OSS_GET_LIVE_CHANNEL = "oss:GetLiveChannel";

	/** 获取指定LiveChannel的推流状态信息。 */
	public static final String ACTION_OSS_GET_LIVE_CHANNEL_STAT = "oss:GetLiveChannelStat";

	/** 获取指定LiveChannel的推流记录。 */
	public static final String ACTION_OSS_GET_LIVE_CHANNEL_HISTORY = "oss:GetLiveChannelHistory";

	/** 为指定的LiveChannel生成一个点播用的播放列表。 */
	public static final String ACTION_OSS_POST_VOD_PLAYLIST = "oss:PostVodPlaylist";

	/** 查看指定LiveChannel在指定时间段内推流生成的播放列表。 */
	public static final String ACTION_OSS_GET_VOD_PLAYLIST = "oss:GetVodPlaylist";

	/** 将音频和视频数据流推送到RTMP。 */
	public static final String ACTION_OSS_PUBLISH_RTMP_STREAM = "oss:PublishRtmpStream";

	/** 基于图片AI技术检测图片标签和置信度。 */
	public static final String ACTION_OSS_PROCESS_IMM = "oss:ProcessImm";

	/** 保存处理后的图片至指定Bucket。 */
	public static final String ACTION_OSS_POST_PROCESS_TASK = "oss:PostProcessTask";

	/** 复制过程涉及的读权限。即允许OSS读取源Bucket和目标Bucket中的数据与元数据，包括Object、Part、Multipart Upload等。 */
	public static final String ACTION_OSS_REPLICATE_GET = "oss:ReplicateGet";

	/**
	 * 复制过程涉及的写权限。即允许OSS对目标Bucket复制相关的写入类操作，包括写入Object、Multipart
	 * Upload、Part和Symlink，修改元数据信息等。
	 */
	public static final String ACTION_OSS_REPLICATE_PUT = "oss:ReplicatePut";

	/**
	 * 复制过程涉及的删除权限。即允许OSS对目标Bucket复制相关的删除操作，包括DeleteObject、AbortMultipartUpload、DeleteMarker等。
	 */
	public static final String ACTION_OSS_REPLICATE_DELETE = "oss:ReplicateDelete";

	// endregion

}
