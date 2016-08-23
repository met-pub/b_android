package com.techjumper.polyhomeb.net;


import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.LoginEntity;
import com.techjumper.polyhomeb.entity.PropertyComplainDetailEntity;
import com.techjumper.polyhomeb.entity.PropertyComplainEntity;
import com.techjumper.polyhomeb.entity.PropertyPlacardDetailEntity;
import com.techjumper.polyhomeb.entity.PropertyPlacardEntity;
import com.techjumper.polyhomeb.entity.PropertyRepairDetailEntity;
import com.techjumper.polyhomeb.entity.PropertyRepairEntity;
import com.techjumper.polyhomeb.entity.SectionsEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.UploadPicEntity;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public interface ServiceAPI {

    /**
     * 发送验证码
     * <p>
     * params:
     * mobile # 手机号码, *表示必填, 下同。
     * type # 短信类型：1(缺省)-注册，2-找回密码。
     * return:
     * result: true # 这里表示成功返回状态时data的内容。
     * <p>
     * # ERROR CODE
     * error_code: 101,	error_msg: '手机号码不正确！'
     * error_code: 102,	error_msg: '该手机号码已经注册！'
     * error_code: 103,	error_msg: '该手机号码没有注册！'
     * error_code: 104,	error_msg: '操作太频繁，请稍后再试！'
     */
    @GET("send_captcha")
    Observable<TrueEntity> sendVerificationCode(@QueryMap Map<String, String> args);

    /**
     * 注册
     * <p>
     * params:
     * mobile # 手机号码
     * sms_captcha # 手机码验证码
     * password # 密码
     * return:
     * id: 1
     * mobile: "18030405923"
     * ticket: "5190bce3e93635aa4d0cb45f664d59936bcbfb1a" #登录过期票据，有效期5天
     * <p>
     * # ERROR CODE
     * error_code: 101,	error_msg: '手机号码不正确！'
     * error_code: 106,	error_msg: '密码不能少于8位！'
     * error_code: 107,	error_msg: '验证码错误！'post '/register'
     */
    @POST("register")
    Observable<LoginEntity> regist(@Body BaseArgumentsEntity entity);

    /**
     * 登陆
     * <p>
     * params:
     * mobile # 手机号码
     * password # 密码
     * return:
     * 同注册接口
     * <p>
     * # ERROR CODE
     * error_code: 103,	error_msg: '该手机号码没有注册'
     * error_code: 108,	error_msg: '用户名或密码错误！'
     */
    @POST("login")
    Observable<LoginEntity> login(@Body BaseArgumentsEntity entity);

    /**
     * 找回密码
     * post '/password/reset'
     * params:
     * mobile # 手机号码
     * sms_captcha # 验证码
     * new_password # 新密码
     * return:
     * result: true
     * # ERROR CODE
     * error_code: 101,	error_msg: '手机号码不正确！'
     * error_code: 103,	error_msg: '该手机号码没有注册'
     * error_code: 106,	error_msg: '密码不能少于8位！'
     * error_code: 107,	error_msg: '验证码错误！'
     */
    @POST("password/reset")
    Observable<TrueEntity> findPassword(@Body BaseArgumentsEntity entity);

    /**
     * 物业管理-物业公告列表
     * <p>
     * get '/notices'
     * <p>
     * params:
     * user_id # 用户ID
     * family_id:1,
     * ticket # session登录验证
     * page: 1 #页号
     * count:3 #每页数据条数
     * "data": {
     * "notices": [
     * {
     * "id": 10,    #公告ID
     * "title": "54trerf",   #公告标题
     * "types": 1,  #公告类型 1-公告 2-资讯
     * "content": "u003cpu003e3243erewu003c/pu003e", #内容
     * "time": "2016-05-06"  #时间
     * },
     * {
     * "id": 9,
     * "title": "tgrs345",
     * "types": 1,
     * "content": "u003cpu003ef34545tu003c/pu003e",
     * "time": "2016-05-06"
     * },
     */
    @GET("notices")
    Observable<PropertyPlacardEntity> propertyNotice(@QueryMap Map<String, String> args);

    /**
     * 公告详情
     * get '/notices/show'
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * id #公告ID
     * return:
     * "error_code": 0,
     * "error_msg": null,
     * "data": {
     * "id": 10,  #公告ID
     * "title": "54trerf", #公告标题
     * "types": 1,  #公告类型 1-公告 2-资讯
     * "content": "u003cpu003e3243erewu003c/pu003e",  #内容
     * "time": "2016-05-06"  #时间
     */
    @GET("notices/show")
    Observable<PropertyPlacardDetailEntity> propertyNoticeDetail(@QueryMap Map<String, String> stringStringMap);

    /**
     * 物业管理-投诉建议列表
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * page: 1 #页号
     * count:3 #每页数据条数
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     */
    @GET("suggestions")
    Observable<PropertyComplainEntity> propertyComplain(@QueryMap Map<String, String> stringStringMap);

    /**
     * get '/repairs'
     * <p>
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * status:0 #报修状态 0-未处理 1-已回复 2-已处理 3-已关闭(查询全部则为空)
     * page: 1 #页号
     * count:3 #每页数据条数
     * <p>
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     */
    @GET("repairs")
    Observable<PropertyRepairEntity> propertyRepair(@QueryMap Map<String, String> stringStringMap);

    /**
     * 新建投诉
     * post '/suggestions'
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * mobile #用户联系电话
     * types #投诉类型 1-投诉 2-建议 3-表扬
     * content #内容
     * imgs: "['img1.jpg','img2.jpg','img3.jpg']"  #多个图片信息（数组）
     * return:
     * {
     * "error_code": 0,
     * "error_msg": null,
     * "data": {
     * "result": "true"
     * }
     * }
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     */
    @POST("suggestions")
    Observable<TrueEntity> newComplain(@Body BaseArgumentsEntity entity);

    /**
     * 新建报修
     * post '/repairs'
     * <p>
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * family_id #家庭ID
     * mobile #用户手机号
     * repair_type #报修类型 1-个人报修 2-公共区域报修
     * repair_device #报修设备 1-门窗类 2-水电类 3-锁类 4-电梯类 5-墙类
     * note #备注
     * imgs: ['img1.jpg','img2.jpg','img3.jpg']  #多个图片信息（数组）
     * return:
     * {
     * "error_code": 0,
     * "error_msg": null,
     * "data": {
     * "result": "true"
     * }
     * }
     */
    @POST("repairs")
    Observable<TrueEntity> newRepair(@Body BaseArgumentsEntity entity);

    /**
     * 上传图片
     * post '/upload/image/base64'
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * file # 上传文件base64编码
     * return:
     * url: # 上传成功返回url
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     * error_code: 208,	error_msg: '上传文件失败'
     */
    @POST("upload/image/base64")
    Observable<UploadPicEntity> uploadPic(@Body BaseArgumentsEntity entity);

    /**
     * 投诉详情(聊天)
     * get '/suggestions/show'
     * <p>
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * id #投诉消息ID
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     * error_code: 404,	error_msg: '未找到内容！'
     */
    @GET("suggestions/show")
    Observable<PropertyComplainDetailEntity> getComplainDetail(@QueryMap Map<String, String> stringStringMap);

    /**
     * 物业管理-投诉建议回复
     * post '/suggestions/reply'
     * <p>
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * content # 回复内容
     * suggestion_id #投诉消息ID
     * return:
     * {
     * "error_code": 0,
     * "error_msg": null,
     * "data": {
     * "result": "true"
     * }
     * }
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     * error_code: 404,	error_msg: '未找到内容！'
     */
    @POST("suggestions/reply")
    Observable<TrueEntity> complainDetailReply(@Body BaseArgumentsEntity entity);


    /**
     * 报修详情
     * get '/repairs/show'
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * repair_id #报修记录ID
     * return:
     * {
     * "error_code": 0,
     * "error_msg": null,
     * "data": {
     * "id": 1
     * "repair_type": 1,
     * "repair_device": 3,
     * "status": 0,
     * "repair_date": "2016-05-16",
     * "note": "备注",
     * "imgs": [      #图片信息
     * '/upload/images/img1.jpg',
     * '/upload/images/img2.jpg',
     * '/upload/images/img3.jpg'
     * ],
     * "replies": [
     * {
     * "user_id": 1,  #用户ID
     * "content": "啦啦啦已提交报修", #回复内容
     * "time": "2016-05-1617: 37" #回复时间
     * },
     * {
     * "user_id": 1,
     * "content": "这里是回复",
     * "time": "2016-05-1617: 44"
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     * error_code: 404,	error_msg: '未找到内容！'
     */
    @GET("repairs/show")
    Observable<PropertyRepairDetailEntity> getRepairDetail(@QueryMap Map<String, String> stringStringMap);


    /**
     * 报修-回复
     * post '/repairs/reply'
     * <p>
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * content # 回复内容
     * repair_id #报修记录ID
     * return:
     * {
     * "error_code": 0,
     * "error_msg": null,
     * "data": {
     * "result": "true"
     * }
     * }
     * <p>
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     * error_code: 404,	error_msg: '未找到内容！'
     */
    @POST("repairs/reply")
    Observable<TrueEntity> repairDetailReply(@Body BaseArgumentsEntity entity);

    /**
     * 回复帖子
     */
    @POST("comment")
    Observable<TrueEntity> replyComment(@Body BaseArgumentsEntity entity);

    /**
     * 友邻-获取版块接口
     */
    @GET("sections")
    Observable<SectionsEntity> getSections(@QueryMap Map<String, String> stringStringMap);

    /**
     * 友邻发帖或者闲置
     */
    @POST("article")
    Observable<TrueEntity> newArticle(@Body BaseArgumentsEntity entity);

}
