package cc.lixiaohui.share.client;

import java.util.Collection;

import cc.lixiaohui.share.client.util.ClientException;
import cc.lixiaohui.share.util.lifecycle.LifeCycle;

/**
 * <pre>
 * 代表客户端
 * 用例:
 * IShareClient client = ShareClientProxy.getInstance("127.0.0.1", 9999);
 * client.send(1, "在吗?");
 * 
 * 返回json说明:
 * 成功时:json中都会包含status, timestamp, msg, result字段:
 * <ul>
 * <li>status: 只有0表示成功, 否则其含义根据接口而异</li>
 * <li>timestamp: 服务器的时间戳</li>
 * <li>msg:操作说明</li>
 * <li>result:操作结果, 具体含义根据接口而异, 注意: 有些接口返回的result为空</li>
 * </ul>
 * 
 * 返回json格式说明: 分成功, 失败两种情况, 无论哪种情况都会有status和timestap两个字段
 * 1.成功时:
 * {
 *   "status":0,
 *   "timestamp":14434243232,
 *   "msg":获取数据成功,
 *   "result":{}    # 内容取决于具体方法
 * }
 * 2.失败时:
 * {
 *   "status":1
 *   "timestamp":1443242343,
 *   "errmsg":"出错了",     							# 出错信息
 *   "errcode":1          							# 错误码
 *   "expmsg":"java.lang.NullPointerException..."	# 异常信息, 该域不一定有值
 * }
 * </pre>
 * @date 2016年10月31日 下午3:53:34
 * @author lixiaohui
 */
public interface IShareClient extends LifeCycle, IImmediateShareClient { 
	
	// *****************************************************************
	// *********************** Log Operations *************************
	// *****************************************************************
	
	/**
	 * 注册普通用户
	 * @param username 用户名
	 * @param password 密码
	 * @return json, 其result如下:
	 * <pre>
	 * {
	 * 		"userId":1,
	 * 		"role": {"roleId":1, "description":"管理员"},
	 * 		"selfShield":false,
	 * 		"adminShield":false
	 * }
	 * </pre>
	 */
	String register(String username, String password) throws ClientException;
	
	/**
	 * 注册管理员账号(只能由Root执行)
	 * @param username
	 * @param password
	 * @return
	 * <pre>
	 * {
	 * 		"userId":1,
	 * 		"role": {"roleId":1, "description":"管理员"},
	 * 		"selfShield":false,
	 * 		"adminShield":false
	 * }
	 * </pre>
	 * @throws ClientException
	 */
	String registerAdmin(String username, String password) throws ClientException;
	
	/**
	 * 登陆
	 * @param username 用户名
	 * @param password 密码
	 * @return json, 其result如下:
	 * <pre>
	 * {
	 * 		"userId":1,
	 * 		"role": {"roleId":1, "description":"管理员"},
	 * 		"selfShield":false,
	 * 		"adminShield":false
	 * }
	 * </pre>
	 */
	String login(String username, String password) throws ClientException;
	
	/**
	 * 注销
	 * @return json, 其result如下:
	 * {
	 * }
	 * @throws ClientException
	 */
	String logout() throws ClientException;
	
	
	// *****************************************************************
	// *********************** User Operations *************************
	// *****************************************************************
	
	/**
	 * 获取用户信息
	 * @param userId 用户ID
	 * @return 如下格式json:
	 * <pre>
	 * {
	 * 	"userId":321                      # 用户ID
	 *  "username":"lixiaohui",           # 用户名
	 *  "sex":"男",                        # 性别
	 *  "signature":"我是好人",              # 个性签名
	 *  "role":{
	 *       "id":32,                      # 角色ID
	 *       "description":"管理员"          # 角色描述
	 *  },
	 *  "registerTime":1478666778,        # 注册时间
	 *  "headImageId":12                  # 头像ID
	 *  "selfShield":false,					# 自己是否屏蔽了自己
	 *  "adminShield":false					# 管理员是否屏蔽了自己
	 * }
	 * </pre>
	 */
	String getUser(int userId) throws ClientException;
	
	/**
	 * 更新用户信息, 要上传
	 * @param password 密码, 传null则不更新
	 * @param sex 性别, 传null则不更新
	 * @param signature 个性签名, 传null则不更新
	 * @param headImageId 头像ID, 传-1则不更新
	 * @return json, result内容如下:
	 * <pre>
	 * {}
	 * </pre>
	 * @throws ClientException
	 */
	String updateUser(String password, String sex, String signature, int headImageId) throws ClientException;
	
	/**
	 * 隔离某用户, 主要是给管理员使用
	 * @param userId 用户ID
	 * @return json, result内容如下:
	 * <pre>
	 * {}
	 * </pre>
	 * @throws ClientException
	 */
	String shield(int userId) throws ClientException;
	
	/**
	 * 根据关键字搜索用户(添加朋友时会用到)
	 * @param keyword 用户名搜索关键字
	 * @return 
	 * <pre>
	 * {
	 *   "count":1,				# 检索到的用户个数
	 *   "users":[
	 *   	{
	 *   		"userId":1,
	 *   		"username":"猪八戒",
	 *   		"sex":"男",
	 *   		"signature":"呵呵",
	 *   		"role":{"roleId":1, "description":"普通用户"}
	 *   		"registerTime":144324324323,
	 *   		"headImageId":1
	 *   		"selfShield":false,
	 *   		"adminShield":false		
	 *   	}
	 *   ]
	 * }
	 * </pre>
	 * @throws ClientException
	 */
	String searchUser(String keyword, int start, int limit) throws ClientException;
	
	
	// *****************************************************************
	// *********************** Share Operations ************************
	// *****************************************************************
	
	/**
	 * 获取share列表
	 * @param keyword 搜索关键字
	 * @param userId 用户ID, 若要获取某个用户的分享时才传, 否则传-1
	 * @param baseTime 时间戳, 获取最新分享时的时间界限, 无需此限制则传-1
	 * @param orderColumn 排序方式的ID, 
	 * @param orderType 降序为0, 升序为1, 默认为0
	 * @param start 起始条数
	 * @param limit 要获取的条数
	 * @param deleted true获取逻辑删除的记录, 默认为false
	 * @return json, result内容如下:
	 * <pre>
	 *  { 
	 *   "count":2,    # 所返回的结果条数
	 *   "shares": [      # 结果数组
	 *   	{
	 *   	 "hasRead":0,					 # 当前用户是否读过该分享, 未登陆时该字段无效
	 *   	 "readCount":100,				 # 该分享的浏览总数
	 *   	 "share":
	 *   		{
	 *   	 		"id":11,                  # 分享ID
	 *   	 		"userId":222,             # 所属用户ID
	 *       		"username":lixiaohui,     # 所属用户名
	 *       		"content":"nice day!!",   # 分享文字内容
	 *       		"createTime":14743432423, # 分享创建时间
	 *       		"praiseCount":23,         # 点赞数
	 *       		"commentCount":32,        # 评论数
	 *       		"pictures":[12,32,43,43]  # 分享包含的图片, 请另外使用getPicture(int pictureId)获取图片字节流
	 *          }
	 *      },
	 *      {
	 *       "hasRead":0,
	 *       "readCount":100,
	 *       "share":
	 *       	{
	 *       		"id":12,                  # 分享ID
	 *     	 		"userId":333,             # 所属用户ID
	 *       		"username":lixiaohui,     # 所属用户名
	 *       		"content":"bad day!!",    # 分享文字内容
	 *       		"createTime":14743432423, # 分享创建时间
	 *       		"praiseCount":23,         # 点赞数
	 *       		"commentCount":32,        # 评论数
	 *       		"pictures":[12,32,43,43]  # 分享包含的图片, 请另外使用getPicture(int pictureId)获取图片字节流
	 *      	}
	 *      }
	 *   ]
	 * }
	 * </pre>
	 */
	String getShares(String keyword, int userId, long baseTime, int orderColumn, int orderType, int start, int limit, boolean deleted) throws ClientException;

	/**
	 * 获取某个分享的所有信息
	 * @param shareId 分享ID
	 * @return json, result内容如下:
	 * <pre>
	 *     {
	 *       "id":321,                 # 分享的ID
	 *       "userId":12,              # 所属用户ID
	 *       "username":lixiaohui,     # 所属用户名
	 *       "content":"bad day!!",    # 分享文字内容
	 *       "createTime":14743432423, # 分享创建时间
	 *       "readCount":100,			# 浏览数
	 *       "praiseInfo":             # 点赞信息
	 *         {
	 *           "praiseCount":2,      # 赞的个数
	 *           "praiseUsers":        # 所有点赞的用户
	 *             [
	 *               {"userId":1, "username":"李小辉"},
	 *               {"userId":2, "username":"李大辉"}
	 *             ]
	 *         }
	 *       "comments":               # 评论信息
	 *         {
	 *           "commentCount":1,     # 评论总数
	 *           "commentList":
	 *             [
	 *               {
	 *                 "commentId":1,             # 评论ID
	 *                 "shareId":2,               # 所属分享的ID
	 *                 "content":"哈哈啊哈哈",       # 评论的内容
	 *                 "kind":1,                  # 评论的类型, 0 = 直接评论的分享, 1 = 回复别人的评论
	 *                 "commentTime":1467326722,  # 评论时间
	 *                 "fromUserId":1             # 评论者ID
	 *                 "fromUsername":"lixiaohui" # 评论者用户名
	 *                 "toUserId":2               # 被评论的用户ID
	 *                 "toUsername":"zhangsan"    # 被评论的用户ID
	 *               }
	 *             ]
	 *         }
	 *       "pictures":[12,32,43,43]  # 分享包含的图片, 请另外使用getPicture(int pictureId)获取图片字节流
	 *     }
	 * }
	 * </pre>
	 * @throws ClientException
	 */
	String getShare(int shareId) throws ClientException;
	
	/**
	 * 删除分享
	 * @param shareId 分享ID
	 * @param physically false = 逻辑删除, true = 物理删除
	 * @return json, result内容如下:
	 * <pre>
	 * {}
	 * </pre>
	 * @throws ClientException
	 */
	String deleteShare(int shareId, boolean physically) throws ClientException;
	
	/**
	 * 发布分享
	 * @param content 分享的文字内容
	 * @param picturesIds 分享所包含的图片的ID列表
	 * @return json, result内容如下:
	 * <pre>
	 * {
	 *     "id":321                  # 发布的分享的ID
	 * }
	 * </pre>
	 * @throws ClientException
	 */
	String publishShare(String content, int[] picturesIds) throws ClientException;
	
	
	/**
	 * 获取我点赞过的分享
	 * @param start 起始条数
	 * @param limit 总条数
	 * @return 
	 * <pre>
	 * {
        "count":1,
        "list":[
            {
                "praiseTime":1480080895000,
                "id":7,
                "share":{
                    "content":"不好",
                    "id":12,
                    "createTime":1479650781000,
                    "praiseCount":1,
                    "username":"李酷酷",
                    "userId":11,
                    "commentCount":0
                },
                "userId":6
            }
        ]
       }
	 * </pre>
	 * @throws ClientException
	 */
	String getLikedShares(int start, int limit) throws ClientException;
	
	/**
	 * 获取我评论过的分享
	 * @param start 起始条数
	 * @param limit 总条数
	 * @return
	 * <pre>
	 * {
        "count":1,
        "list":[
            {
                "content":"lureogjgdlk",
                "id":6,
                "share":{
                    "content":"的洒家打开了打卡时间的咯",
                    "id":4,
                    "createTime":1479046241000,
                    "praiseCount":0,
                    "username":"小明",
                    "userId":5,
                    "commentCount":1
                },
                "fromUsername":"小明",
                "commentTime":1480072104000,
                "toUserId":5,
                "toUsername":"小明",
                "fromUserId":5
            }
        ]
    }
	 * </pre>
	 * @throws ClientException
	 */
	String getCommentedShares(int start, int limit) throws ClientException;
	
	
	// *****************************************************************
	// *********************** Comment Operations **********************
	// *****************************************************************
		
	/**
	 * 获取分享的评论
	 * 
	 * @param shareId 分享ID
	 * @param start 起始条数, 若要获取全部则给start或者limit传负值即可
	 * @param limit 总条数, 若要获取全部则给start或者limit传负值即可
	 * @return json, result内容如下:
	 * <pre>
	 *  {
	 *   "count":2
	 *   "list":[
	 *    {
	 *     "id":11,                   # 评论ID
	 *     "shareId":32               # 所属分享的ID
	 *     "content":"good!!",        # 评论的内容
	 *     "kind":0                   # 评论的类型, 0 = 直接评论的分享, 1 = 回复别人的评论
	 *     "commentTime":1465565576,  # 评论时间
	 *     "fromUserId":1             # 评论者ID
	 *     "fromUsername":"lixiaohui" # 评论者用户名
	 *     "toUserId":2               # 被评论的用户ID
	 *     "toUsername":"zhangsan"    # 被评论的用户ID
	 *    },
	 *    {
	 *     "id":11,                   # 评论ID
	 *     "shareId":32               # 所属分享的ID
	 *     "content":"good!!",        # 评论的内容
	 *     "kind":0                   # 评论的类型, 0 = 直接评论的分享, 1 = 回复别人的评论
	 *     "commentTime":1465565576,  # 评论时间
	 *     "fromUserId":1             # 评论者ID
	 *     "fromUsername":"lixiaohui" # 评论者用户名
	 *     "toUserId":2               # 被评论的用户ID
	 *     "toUsername":"zhangsan"    # 被评论的用户名
	 *    }
	 *   ]
	 * }
	 * </pre>
	 */
	String getComments(int shareId, int start, int limit) throws ClientException;
	
	/**
	 * 
	 * @param commentId 评论ID
	 * @return json, result内容如下:
	 * <pre>
	 * {}
	 * </pre>
	 * @throws ClientException
	 */
	String deleteComment(int commentId) throws ClientException;
	
	/**
	 * 评论某分享
	 * @param shareId 所评论的分享的ID
	 * @param content 评论内容
	 * @param toUserId 回复的目标用户的ID
	 * @return json, result内容如下:
	 * <pre>
	 * {
	 *   "commentId":1
	 * }
	 * </pre>
	 * @throws ClientException
	 */
	String publishComment(int shareId, int toUserId, String content) throws ClientException;
	
	
	
	// *****************************************************************
	// *********************** Praise Operations **********************
	// *****************************************************************
	
	/**
	 * 点赞
	 * @param shareId 分享ID
	 * @return
	 * <pre>
	 * {}
	 * </pre>
	 * @throws ClientException
	 */
	String like(int shareId) throws ClientException;
	
	/**
	 * 取消点赞
	 * @param praiseId 赞的ID
	 * @return 
	 * <pre>
	 * {}
	 * </pre>
	 * @throws ClientException
	 */
	String unlike(int praiseId) throws ClientException;
	
	
	// *****************************************************************
	// *********************** Friends Operations **********************
	// *****************************************************************
	
	/**
	 * 获取好友列表
	 * @param userId 用户ID
	 * @param start 起始条数
	 * @param limit 总条数
	 * @return json, result内容如下:
	 * <pre>
	 *     {
	 *       "count":2,
	 *       "list":
	 *         [
	 *           {
	 *             "id":21,                    # 好友的用户ID
	 *             "time":1466757677,          # 成为好友的时间
	 *             "username":"lisi",          # 好友用户名
	 *             "sex":"男",
	 *             "signature":"",
	 *             "role":2						# 角色ID
	 *             "registerTime":144342342    # 用户注册时间
	 *             "headImageId":13            # 头像ID
	 *           },
	 *           {
	 *             "id":32,                    # 好友的用户ID
	 *             "time":1466757677,          # 成为好友的时间
	 *             "username":"lisi",          # 好友用户名
	 *             "sex":"男",
	 *             "signature":"dads",
	 *             "roleId":2
	 *             "registerTime":144342342    # 用户注册时间
	 *             "headImageId":13            # 头像ID
	 *           }
	 *         ]
	 *     }
	 * </pre>
	 */
	String getFriends(int start, int limit) throws ClientException;
	
	/**
	 * 删除好友
	 * @param friendId 好友关系的ID(不是好友的ID)
	 * @return json, result内容如下:
	 * {}
	 * @throws ClientException
	 */
	String deleteFriend(int friendShipId) throws ClientException;
	
	/**
	 * 添加好友请求
	 * @param targetUserId 目标用户ID
	 * @return
	 * <pre>
	 * {
	 * }
	 * </pre>
	 * @throws ClientException
	 */
	String addFriend(int targetUserId) throws ClientException;
	
	/**
	 * 处理他人发来的好友请求
	 * 
	 * @param friendShipId 
	 * @param accept 是否同意
	 * @return
	 * <pre>
	 * {}
	 * </pre>
	 * @throws ClientException
	 */
	String handleFriendRequest(int friendShipId, boolean accept) throws ClientException;
	
	// *****************************************************************
	// *********************** Picture Operations **********************
	// *****************************************************************
	
	/**
	 * 上传单张图片
	 * @param bytes 图片字节
	 * @return json, result内容如下:
	 * <pre>
	 * {
	 *     "id":2
	 * }
	 * </pre>
	 * @throws ClientException
	 */
	String uploadPicture(String suffix, byte[] bytes) throws ClientException;
	
	/**
	 * 根据ID获取图片
	 * @param pictureId 图片ID
	 * @param ignoreIfNotExist 当给定ID数组中存在某些ID不存在时是跳过还是返回错误
	 * @return json, result内容如下:
	 * <pre>
	 *     {
	 *       "count":2,
	 *       "list":
	 *         [
	 *           {
	 *             "id":32,                    # 图片ID
	 *             "bytes":dshdssad,           # 图片字节
	 *             "uploadTime":14678787787,   # 图片上传时间
	 *             "uploadUserId":321,         # 上传该图片的用户ID
	 *           },
	 *           {
	 *             "id":33,                    # 图片ID
	 *             "bytes":kjdajkjjk,          # 图片字节
	 *             "uploadTime":14678787787,   # 图片上传时间
	 *             "uploadUserId":321,         # 上传该图片的用户ID
	 *           }
	 *         ]
	 *     }
	 * </pre>
	 */
	String getPictures(boolean ignoreIfNotExist, int[] pictureIds) throws ClientException;
	
	/**
	 * 删除图片
	 * @param pictureId 图片ID
	 * @return json, result内容如下:
	 * <pre>
	 * {}
	 * </pre>
	 * @throws ClientException
	 */
	String deletePicture(int pictureId) throws ClientException;
	
	
	// *****************************************************************
	// ********************* Collection Operations *********************
	// *****************************************************************
	
	/**
	 * 获取收藏的用户
	 * @param start 起始条数
	 * @param limit 总条数
	 * @return json, result内容如下:
	 * <pre>
	 *     {
	 *       "count":1,                          # 获取的用户总数
	 *       "collections":                      # 用户列表
	 *         [
	 *           {
	 *             "collectionId":12,            # 该收藏ID
	 *             "collectTime":14342432423,    # 收藏时间
	 *             "user":                       # 对应的用户
	 *               {
	 *                 "userId":32,              # 用户ID
	 *                 "username":"李晓明",        # 用户名
	 *                 "headImageId":321         # 头像ID
	 *               }
	 *           }
	 *         ]
	 *     }
	 * </pre>
	 * @throws ClientException
	 */
	String getUserCollection(int start, int limit) throws ClientException;
	
	/**
	 * 获取用户收藏的分享
	 * @param start 起始条数
	 * @param limit 总条数
	 * @return json, result内容如下:
	 * <pre>
	 *     {
	 *       "count":1,                                   # 查询到的收藏总数
	 *       "collections":                               # 查询到的收藏列表
	 *         [
	 *           {
	 *             "collectionId":11,
	 *             "collectTime":14432423432,             # 收藏的时间
	 *             "share":                               # 对应的分享
	 *               {
	 *                 "shareId":1,                       # 分享ID
	 *                 "content":321,                     # 该分享的内容
	 *                 "createTime":14342424,             # 该分享创建的时间
	 *                 "praiseCount":432,                 # 赞数
	 *                 "commentCount":312                 # 评论数
	 *               }
	 *           }
	 *         ]
	 *     }
	 * </pre>
	 * @throws ClientException
	 */
	String getShareCollection(int start, int limit) throws ClientException;
	
	/**
	 * 取消对用户的收藏
	 * @param collectionId 收藏项的ID
	 * @return json, result内容如下:
	 * <pre>
	 * {}
	 * </pre>
	 * @throws ClientException
	 */
	String unCollectUser(int collectionId) throws ClientException;
	
	/**
	 * 取消对分享的收藏
	 * @param collectionId 收藏项的ID
	 * @return json, result内容如下:
	 * <pre>
	 * {}
	 * </pre>
	 * @throws ClientException
	 */
	String unCollectShare(int collectionId) throws ClientException;
	
	
	// *****************************************************************
	// ********************* Sortings Operations *********************
	// *****************************************************************

	/**
	 * 获取所有排序方式
	 * @return 返回如下json
	 * <pre>
	 *     {
	 *       "count":2,
	 *       "sortings":
	 *         [
	 *           {
	 *             "id":1,
	 *             "type":赞,
	 *             "description":"按点赞数正序"
	 *           },
	 *           {
	 *             "id":2,
	 *             "type":评论,
	 *             "description":"按评论数正序"
	 *           }
	 *         ]
	 *     }
	 * </pre>
	 * @throws ClientException
	 */
	String getSortings() throws ClientException;
	
	
	// *****************************************************************
	// ********************* ForbidenWords Operations *********************
	// *****************************************************************
	
	/**
	 * 获取敏感词集
	 * @return result格式如下
	 * <pre>
	 * {
	 *  "count":2,
	 *  "words":[
	 *  	{
	 *  		"id":1,
	 *  		"content":"AV"  # 敏感词
	 *  	},
	 *  	{
	 *  		"id":2,
	 *  		"content":"VA"
	 *  	}
	 *  ]
	 * }
	 * </pre>
	 * @throws ClientException
	 */
	String getForbidenWords(int start, int limit) throws ClientException;
	
	/**
	 * 获取已删除的敏感词
	 * @param start
	 * @param limit
	 * @return 和 {@link #getForbidenWords(int, int)} 一样
	 * 
	 * @throws ClientException
	 */
	String getDeletedForbidenWords(int start, int limit) throws ClientException;
	
	/**
	 * 删除敏感词
	 * @param wordId 敏感词ID
	 * @return 
	 * {}
	 * @throws ClientException
	 */
	String deleteForbidendWord(int wordId) throws ClientException;
	
	/**
	 * 恢复删除的敏感词
	 * @param wordId 敏感词ID
	 * @return 
	 * {}
	 * @throws ClientException
	 */
	String recoverForbidendWord(int wordId) throws ClientException;
	
	void addConnectionListener(IConnectionListener l);
	
	void addConnectionListeners(Collection<IConnectionListener> listeners);
	
	void removeConnectionListener(IConnectionListener l);
}
