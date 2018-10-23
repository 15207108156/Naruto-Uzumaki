package com.xiaoyi.ssm.controller.wxapp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.xiaoyi.ssm.dto.ApiMessage;
import com.xiaoyi.ssm.dto.PageBean;
import com.xiaoyi.ssm.model.Member;
import com.xiaoyi.ssm.model.TrainCoach;
import com.xiaoyi.ssm.model.TrainCourse;
import com.xiaoyi.ssm.model.TrainOrder;
import com.xiaoyi.ssm.model.TrainOrderComment;
import com.xiaoyi.ssm.model.TrainTeam;
import com.xiaoyi.ssm.model.TrainTrial;
import com.xiaoyi.ssm.model.Venue;
import com.xiaoyi.ssm.service.TrainCoachService;
import com.xiaoyi.ssm.service.TrainCourseService;
import com.xiaoyi.ssm.service.TrainOrderCommentService;
import com.xiaoyi.ssm.service.TrainOrderService;
import com.xiaoyi.ssm.service.TrainTeamImageService;
import com.xiaoyi.ssm.service.TrainTeamService;
import com.xiaoyi.ssm.service.TrainTrialService;
import com.xiaoyi.ssm.service.VenueService;
import com.xiaoyi.ssm.util.DateUtil;
import com.xiaoyi.ssm.util.Global;
import com.xiaoyi.ssm.util.RedisUtil;
import com.xiaoyi.ssm.util.Utils;
import com.xiaoyi.ssm.wxPay.WXConfig;
import com.xiaoyi.ssm.wxPay.WXPayJsapiUtil;
import com.xiaoyi.ssm.wxPay.WXPayUtil;
import com.xiaoyi.ssm.wxPay.WXPayWxappUtil;
import com.xiaoyi.ssm.wxPay.XMLUtil;

/**
 * @author 宋高俊
 * @Description: 培训 课程接口控制器
 * @date 2018年9月29日 下午7:01:57
 */
@Controller("wxappTrainCourseController")
@RequestMapping("wxapp/train/course")
public class ApiTrainCourseController {

    private static Logger logger = Logger.getLogger(ApiTrainCourseController.class.getName());

    @Autowired
    private TrainTeamService trainTeamService;
    @Autowired
    private VenueService venueService;
    @Autowired
    private TrainCoachService trainCoachService;
    @Autowired
    private TrainCourseService trainCourseService;
    @Autowired
    private TrainTeamImageService trainTeamImageService;
    @Autowired
    private TrainOrderService trainOrderService;
    @Autowired
    private TrainTrialService trainTrialService;
    @Autowired
    private TrainOrderCommentService trainOrderCommentService;

    /**
     * @param request
     * @param id
     * @return
     * @Description: 获取立即报名课程的数据
     * @author 宋高俊
     * @date 2018年9月30日 下午2:30:37
     */
    @RequestMapping(value = "/getTrainCourse")
    @ResponseBody
    public ApiMessage getTrainCourse(HttpServletRequest request, String id) {

        String token = (String) request.getAttribute("token");
        Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);

        TrainCourse trainCourse = trainCourseService.selectByPrimaryKey(id);
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("id", trainCourse.getId()); // ID
        map.put("courseHeadImage", trainCourse.getHeadImage()); // 封面
        map.put("title", trainCourse.getTitle()); // 标题
        map.put("amount", trainCourse.getAmount()); // 价格
        map.put("minAge", trainCourse.getMinAge() + "~" + trainCourse.getMaxAge() + "岁"); // 适合年龄
        map.put("classHour", trainCourse.getClassHour() + "课时"); // 价格
        map.put("toTeachNumber", trainCourse.getToTeachNumber() + "人以下"); // 适合人数

        Venue trainVenue = venueService.selectByPrimaryKey(trainCourse.getTrainVenueId());
        map.put("venueTitle", trainVenue.getName()); // 培训球馆

        TrainCoach trainCoach = trainCoachService.selectByPrimaryKey(trainCourse.getTrainCoachId());
        map.put("coachHeadImage", trainCoach.getHeadImage()); // 头像
        map.put("name", trainCoach.getName()); // 姓名
        

        map.put("payTrial", trainCourse.getPayTrial()); // 试课说明
        
        return new ApiMessage(200, "获取成功", map);
    }

    /**
     * @Description: 提交订单数据, 并返回支付参数
     * @param request
     * @param id
     * @return
     * @author 宋高俊
     * @date 2018年9月30日 下午2:30:37
     */
    @RequestMapping(value = "/getPayParameter")
    @ResponseBody
    public ApiMessage getPayParameter(HttpServletRequest request, String id, String phone, String content, Integer wxflag) {
        // 用户数据
        String token = (String) request.getAttribute("token");
        Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);

        // 课程数据
        TrainCourse trainCourse = trainCourseService.selectByPrimaryKey(id);

        // 预支付订单
        TrainOrder trainOrder = new TrainOrder();
        trainOrder.setId(Utils.getUUID());
        trainOrder.setAmount(trainCourse.getAmount());
        trainOrder.setCreateTime(new Date());
        trainOrder.setModifyTime(new Date());
        trainOrder.setTrainCourseId(trainCourse.getId());
        trainOrder.setPhone(phone);
        trainOrder.setContent(content);
        trainOrder.setMemberId(member.getId());

        if (trainCourse.getAmount().doubleValue() > 0) {
            trainOrder.setPayType(0);
            trainOrderService.insertSelective(trainOrder);
            Map map = new HashMap();
            if (wxflag == 0) {
            	map = WXPayJsapiUtil.getPayParams("AA退费补录金额", trainOrder.getId(), member.getOpenid(), trainCourse.getAmount().doubleValue(), request.getRemoteAddr(),
                        WXConfig.NOTIFY_URL4);
            } else {
                map = WXPayWxappUtil.getPayParams("AA退费补录金额", trainOrder.getId(), member.getAppopenid(), trainCourse.getAmount().doubleValue(), request.getRemoteAddr(),
                        WXConfig.NOTIFY_URL4);
            }
            return new ApiMessage(200, "获取成功", map);
        } else {
            trainOrder.setPayType(1);
            trainOrderService.insertSelective(trainOrder);
            return new ApiMessage(201, "报名成功");
        }
    }

    /**
     * @Description: 培训支付完成回调
     * @param request
     * @param response
     * @throws IOException
     * @throws JDOMException
     * @author 宋高俊
     * @date 2018年9月14日 上午10:43:55
     */
    @SuppressWarnings({"unchecked"})
    @RequestMapping(value = "/weixinNotify", method = RequestMethod.POST)
    public void weixinNotify(HttpServletRequest request, HttpServletResponse response) throws IOException, JDOMException {
        logger.info("培训支付回调");
        // 读取参数
        InputStream inputStream = request.getInputStream();
        StringBuffer sb = new StringBuffer();
        String string;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((string = in.readLine()) != null) {
            sb.append(string);
        }
        in.close();
        inputStream.close();
        logger.info("xml参数 = " + sb.toString());
        // 解析xml成map
        Map<String, String> packageParams = new HashMap<String, String>();
        packageParams = XMLUtil.doXMLParse(sb.toString());

        // 判断签名是否正确
        if (WXPayUtil.isTenpaySign("UTF-8", packageParams, WXConfig.paternerKey)) {

            logger.info("微信支付成功回调");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            String resXml = "";
            if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
                // 这里是支付成功
                String orderNo = (String) packageParams.get("out_trade_no");
                String total_fee = (String) packageParams.get("total_fee");
                logger.info("商户订单号" + orderNo + "付款成功,金额" + total_fee);
                // 这里 根据实际业务场景 做相应的操作
                // 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                // 这一步开始就是写公司业务需要的代码了，不用参考我的 没有价值

                // 查询订单
                TrainOrder trainOrder = trainOrderService.selectByPrimaryKey("orderNo");
                if (trainOrder.getPayType() != 1) {
                    trainOrder.setPayType(1);
                    trainOrderService.updateByPrimaryKeySelective(trainOrder);
                }

                logger.info("微信订单号" + orderNo + "付款成功");
            } else {
                logger.info("支付失败,错误信息：" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
            // ------------------------------
            // 处理业务完毕
            // ------------------------------
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
        } else {
            logger.info("通知签名验证失败");
        }
    }

    /**
     * @param request
     * @param id
     * @return
     * @Description: 提交免费试课的订单数据
     * @author 宋高俊
     * @date 2018年9月30日 下午2:30:37
     */
    @RequestMapping(value = "/addTrainTrial")
    @ResponseBody
    public ApiMessage addTrainTrial(HttpServletRequest request, String id, String phone, String content, Integer wxflag) {
        // 用户数据
        String token = (String) request.getAttribute("token");
        Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);

        // 课程数据
        TrainCourse trainCourse = trainCourseService.selectByPrimaryKey(id);
        // 培训机构是否允许免费试课
        TrainTeam trainTeam = trainTeamService.selectByPrimaryKey(trainCourse.getTrainCoachId());
        if (trainTeam.getFreeClasses() == 1) {
            // 预支付订单
            TrainTrial trainTrial = new TrainTrial();
            trainTrial.setId(Utils.getUUID());
            trainTrial.setCreateTime(new Date());
            trainTrial.setModifyTime(new Date());
            trainTrial.setTrainCourseId(trainCourse.getId());
            trainTrial.setPhone(phone);
            trainTrial.setContent(content);
            trainTrial.setMemberId(member.getId());
            trainTrialService.insertSelective(trainTrial);
            return new ApiMessage(200, "预约成功");
        } else {
            return new ApiMessage(400, "该机构不允许免费预约课程");
        }
    }

    /**
     * @param request
     * @return
     * @Description: 获取我的课程
     * @author 宋高俊
     * @date 2018年10月8日 下午2:48:07
     */
    @RequestMapping(value = "/manager/getMyCourse")
    @ResponseBody
    public ApiMessage getMyCourse(HttpServletRequest request) {
        // 用户数据
        String token = (String) request.getAttribute("token");
        Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);
        // 根据用户ID查询教练数据
        TrainCoach trainCoach = trainCoachService.selectByMemberId(member.getId());
        List<TrainCourse> listCourses = trainCourseService.selectByTrainCoachID(trainCoach.getId());

        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < listCourses.size(); i++) {
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("id", listCourses.get(i).getId()); // ID
            map.put("headImage", listCourses.get(i).getHeadImage()); // 封面
            map.put("title", listCourses.get(i).getTitle()); // 课程名称
            map.put("classHour", listCourses.get(i).getClassHour()); // 课时
            map.put("amount", listCourses.get(i).getAmount()); // 价格
            map.put("applyPeopleSum", listCourses.get(i).getApplyPeopleSum()); // 报名人数
            listmap.add(map);
        }
        return new ApiMessage(200, "获取成功", listmap);
    }

    /**
     * @param request
     * @return
     * @Description: 新增我的课程
     * @author 宋高俊
     * @date 2018年10月8日 下午2:48:07
     */
    @RequestMapping(value = "/manager/addMyCourse")
    @ResponseBody
    public ApiMessage addMyCourse(HttpServletRequest request, TrainCourse trainCourse) {
        // 用户数据
        String token = (String) request.getAttribute("token");
        Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);
        // 根据用户ID查询教练数据
        TrainCoach trainCoach = trainCoachService.selectByMemberId(member.getId());
        trainCourse.setId(Utils.getUUID());
        trainCourse.setCreateTime(new Date());
        trainCourse.setModifyTime(new Date());
        trainCourse.setTrainCoachId(trainCoach.getId());

        int flag = trainCourseService.insertSelective(trainCourse);
        if (flag > 0) {
            return new ApiMessage(200, "新增成功", trainCourse.getId());
        } else {
            return new ApiMessage(400, "新增失败");
        }
    }
    
    /**  
     * @Description: 获取我的教学场地
     * @author 宋高俊  
     * @param request
     * @return 
     * @date 2018年10月13日 上午10:04:55 
     */ 
    @RequestMapping(value = "/manager/getMyTeamVenue")
    @ResponseBody
    public ApiMessage getMyTeamVenue(HttpServletRequest request) {
        // 用户数据
        String token = (String) request.getAttribute("token");
        Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);
        // 根据用户ID查询教练数据
        TrainCoach trainCoach = trainCoachService.selectByMemberId(member.getId());
        // 场地列表
        List<Venue> list = venueService.selectByTrainTeamID(trainCoach.getTrainTeamId());

        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", list.get(i).getId());
			map.put("title", list.get(i).getName());
			listmap.add(map);
		}
        return new ApiMessage(200, "新增成功", listmap);
    }
    
    /**
     * @Description: 获取要修改的课程详情
     * @param request
     * @return
     * @author 宋高俊
     * @date 2018年10月8日 下午2:48:07
     */
    @RequestMapping(value = "/manager/editMyCourse")
    @ResponseBody
    public ApiMessage updateMyCourse(HttpServletRequest request, String id) {
//        // 用户数据
//        String token = (String) request.getAttribute("token");
//        Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);
//        // 根据用户ID查询教练数据
//        TrainCoach trainCoach = trainCoachService.selectByMemberId(member.getId());
        TrainCourse oldTrainCourse = trainCourseService.selectByPrimaryKey(id);
        return new ApiMessage(200, "修改成功",oldTrainCourse);
    }

    /**
     * @Description: 修改我的课程
     * @param request
     * @return
     * @author 宋高俊
     * @date 2018年10月8日 下午2:48:07
     */
    @RequestMapping(value = "/manager/updateMyCourse")
    @ResponseBody
    public ApiMessage updateMyCourse(HttpServletRequest request, TrainCourse trainCourse) {
        // 用户数据
        String token = (String) request.getAttribute("token");
        Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);
        // 根据用户ID查询教练数据
        TrainCoach trainCoach = trainCoachService.selectByMemberId(member.getId());

        TrainCourse oldTrainCourse = trainCourseService.selectByPrimaryKey(trainCourse.getId());
        if (!oldTrainCourse.getTrainCoachId().equals(trainCoach.getId())) {
            return new ApiMessage(400, "无权限修改");
        }
        trainCourse.setModifyTime(new Date());

        int flag = trainCourseService.updateByPrimaryKeySelective(trainCourse);
        if (flag > 0) {
            return new ApiMessage(200, "修改成功");
        } else {
            return new ApiMessage(400, "修改失败");
        }
    }

    /**
     * @param request
     * @param id      课程ID
     * @return
     * @Description: 获取学员列表
     * @author 宋高俊
     * @date 2018年10月9日 上午10:37:03
     */
    @RequestMapping(value = "/manager/getMyCourse/student")
    @ResponseBody
    public ApiMessage getMyCourseStudent(HttpServletRequest request, String id) {

        // 根据课程id查询报名人员
        List<TrainOrder> trainOrders = trainOrderService.selectByTrainCourseID(id);

        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < trainOrders.size(); i++) {
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("id", trainOrders.get(i).getId()); // ID
            map.put("appavatarurl", trainOrders.get(i).getMember().getAppavatarurl()); // 头像
            map.put("appnickname", trainOrders.get(i).getMember().getAppnickname()); // 昵称
            map.put("classHour", trainOrders.get(i).getClassHour()); // 当前课时
            map.put("classHourSum", trainOrders.get(i).getClassHourSum()); // 总课时
            listmap.add(map);
        }
        return new ApiMessage(200, "获取成功", listmap);
    }

    /**
     * @param request
     * @param id      订单ID
     * @return
     * @Description: 评价邀请接口
     * @author 宋高俊
     * @date 2018年10月9日 下午2:31:41
     */
    @RequestMapping(value = "/manager/commentInvite")
    @ResponseBody
    public ApiMessage commentInvite(HttpServletRequest request, String id) {

        // 用户数据
        String token = (String) request.getAttribute("token");
        Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);

        // 教练数据
        TrainCoach trainCoach = trainCoachService.selectByMemberId(member.getId());

        // 查询订单是否存在
        TrainOrder trainOrder = trainOrderService.selectByPrimaryKey(id);
        if (trainOrder == null) {
            return new ApiMessage(400, "订单不存在");
        }
        // 根据课时和订单ID查询是否已经发送过评价邀请
        TrainOrderComment oldTrainOrderComment = trainOrderCommentService.selectByHourOrder((trainOrder.getClassHour() + 1), id);
        if (oldTrainOrderComment != null) {
            return new ApiMessage(400, "该课时已发送邀请评价,请耐心等候评价");
        }
        TrainOrderComment trainOrderComment = new TrainOrderComment();
        trainOrderComment.setId(Utils.getUUID());
        trainOrderComment.setCreateTime(new Date());
        trainOrderComment.setModifyTime(new Date());
        trainOrderComment.setClassHour(trainOrder.getClassHour() + 1);
        trainOrderComment.setState(0);
        trainOrderComment.setTrainOrderId(id);
        trainOrderComment.setTrainCoachId(trainCoach.getId());
        trainOrderComment.setMemberId(trainOrder.getMemberId());
        int flag = trainOrderCommentService.insertSelective(trainOrderComment);
        if (flag > 0) {
            return new ApiMessage(200, "邀请成功");
        } else {
            return new ApiMessage(400, "邀请失败");
        }
    }

    /**
     * @Description: 获取我的私教列表
     * @param request
     * @return
     * @author 宋高俊
     * @date 2018年10月9日 上午10:37:03
     */
    @RequestMapping(value = "/manager/getMyCoach")
    @ResponseBody
    public ApiMessage getMyCoach(PageBean pageBean, HttpServletRequest request) {
        PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
        // 用户数据
        String token = (String) request.getAttribute("token");
        Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);

        // 获取我的私教数据
        List<TrainOrder> trainOrders = trainOrderService.selectByMember(member.getId());

        List<Map<String, Object>> listmap = new ArrayList<>();
        for (int i = 0; i < trainOrders.size(); i++) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", trainOrders.get(i).getId()); // 订单ID

            TrainCourse trainCourse = trainCourseService.selectByPrimaryKey(trainOrders.get(i).getTrainCourse().getId());
            TrainCoach trainCoach = trainCoachService.selectByPrimaryKey(trainCourse.getTrainCoachId());
            
            map.put("createTime", DateUtil.getFormat(trainOrders.get(i).getCreateTime())); // 报名时间
            map.put("headImage", trainCoach.getHeadImage()); // 教练头像
            map.put("tramTitle", trainCoach.getTrainTeam().getTitle()); // 培训机构名称
            map.put("ballType", trainOrders.get(i).getTrainCourse().getBallType()); // 类别(1=网球2=足球3=羽毛球)
            map.put("name", trainCoach.getName()); // 教练名称
            map.put("type", trainCoach.getType()); // 教练身份
            map.put("title", trainOrders.get(i).getTrainCourse().getTitle()); // 课程名称
            map.put("hourSum", trainOrders.get(i).getClassHourSum()); // 课时数量

            // 判断是否有评价邀请
            TrainOrderComment trainOrderComment = trainOrderCommentService.selectByHourOrder(trainOrders.get(i).getClassHour() + 1, trainOrders.get(i).getId());
            if (trainOrderComment != null) {
                map.put("commentFlag", 1); // 有需要评价的邀请
                map.put("orderCommentId", trainOrderComment.getId()); // 评价邀请的ID
            } else {
                map.put("commentFlag", 0); // 有需要评价的邀请
            }
            listmap.add(map);
        }
        return new ApiMessage(200, "获取成功", listmap);
    }

    /**  
     * @Description: 保存对课程培训的评价
     * @author 宋高俊  
     * @param request
     * @param id
     * @param commentSelect
     * @param centent
     * @return 
     * @date 2018年10月10日 上午11:22:54 
     */ 
    @RequestMapping(value = "/manager/saveComment")
    @ResponseBody
    public ApiMessage saveComment(HttpServletRequest request, String id, Integer commentSelect, String centent) {
        // 用户数据
        String token = (String) request.getAttribute("token");
        Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);

        TrainOrderComment oldtoc = trainOrderCommentService.selectByPrimaryKey(id);
        if (oldtoc == null) {
            return new ApiMessage(400, "该评价不存在");
        } else {
            if (oldtoc.getState() == 1 || oldtoc.getState() == 2) {
                return new ApiMessage(400, "该评价已操作，不能重复操作");
            }
        }

        TrainOrderComment toc = new TrainOrderComment();

        toc.setId(id);
        toc.setModifyTime(new Date());
        toc.setCommentSelect(commentSelect);
        toc.setContent(centent);
        toc.setState(1);

        int flag = trainOrderCommentService.updateByPrimaryKeySelective(toc);
        if (flag > 0) {
            return new ApiMessage(200, "评价成功");
        } else {
            return new ApiMessage(400, "评价失败");
        }
    }

    /**
     * @Description: 培训机构>>我的私教列表>>评价详情
     * @author 宋高俊
     * @date 2018/10/9 0009 20:07
     */
    @RequestMapping(value = "/manager/commentList")
    @ResponseBody
    public ApiMessage commentList(HttpServletRequest request, String id) {
        // 用户数据
        String token = (String) request.getAttribute("token");
        Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);

        TrainOrder trainOrder = trainOrderService.selectByPrimaryKey(id);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", trainOrder.getId()); // 订单ID

        TrainCourse trainCourse = trainCourseService.selectByPrimaryKey(trainOrder.getTrainCourse().getId());
        TrainCoach trainCoach = trainCoachService.selectByPrimaryKey(trainCourse.getTrainCoachId());

        map.put("tramTitle", trainCoach.getTrainTeam().getTitle()); // 培训机构名称
        map.put("ballType", trainOrder.getTrainCourse().getBallType()); // 类别(1=网球2=足球3=羽毛球)
        map.put("name", trainCoach.getName()); // 教练名称
        map.put("type", trainCoach.getType()); // 教练身份1=主教2助教
        map.put("title", trainOrder.getTrainCourse().getTitle()); // 课程名称
        map.put("hourSum", trainOrder.getClassHourSum()); // 课时数量

        List<TrainOrderComment> list = trainOrderCommentService.selectByOrder(id);
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> mapo = new HashMap<>();
            mapo.put("appavatarurl", list.get(i).getMember().getAppavatarurl()); // 头像
            mapo.put("time", DateUtil.getFormat(list.get(i).getModifyTime())); // 评价时间
            mapo.put("commentSelect", list.get(i).getCommentSelect()); // 评价选择(1=好评2=中评3=差评4=拒绝评价)
            mapo.put("content", list.get(i).getContent()); // 内容
            listMap.add(mapo);
        }
        map.put("list", listMap);

        return new ApiMessage(200, "获取成功", map);
    }
}