package com.xiaoyi.ssm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaoyi.ssm.dao.AmountRefundWayMapper;
import com.xiaoyi.ssm.dao.VenueMapper;
import com.xiaoyi.ssm.model.Member;
import com.xiaoyi.ssm.model.TrainTeamVenue;
import com.xiaoyi.ssm.model.Venue;
import com.xiaoyi.ssm.service.FieldService;
import com.xiaoyi.ssm.service.VenueService;

/**  
 * @Description: 场馆业务逻辑实现
 * @author 宋高俊  
 * @date 2018年8月16日 下午6:00:43 
 */ 
@Service
public class VenueServiceImpl extends AbstractService<Venue,String> implements VenueService{

	@Autowired
	private VenueMapper venueMapper;
	@Autowired
	private FieldService fieldService;
	@Autowired
	private AmountRefundWayMapper amountRefundWayMapper;
	
	@Override
	public void setBaseMapper() {
	    super.setBaseMapper(venueMapper);
	}
	

	@Override
	public List<Venue> selectByOftenMember(String id) {
		return venueMapper.selectByOftenMember(id);
	}

	/**  
	 * @Description: 根据场馆名查询场馆
	 * @author 宋高俊  
	 * @param venuename
	 * @return 
	 * @date 2018年9月21日 下午8:58:30 
	 */ 
	@Override
	public Venue selectByVenueCity(String venueName, String id) {
		return venueMapper.selectByVenueCity(venueName, id);
	}

	/**  
	 * @Description: 自定义条件查询场馆列表
	 * @author 宋高俊  
	 * @return 
	 * @date 2018年10月8日 上午9:19:58 
	 */ 
	@Override
	public List<Venue> selectByAllVenue(String cityid, String districtid, Double lng, Double lat, Integer ballType) {
		return venueMapper.selectByAllVenue(cityid, districtid, lng, lat, ballType);
	}
	
	
	/** 
	 * @Description: 根据培训机构ID获取培训场地
	 * @author 宋高俊  
	 * @param id
	 * @return 
	 * @date 2018年9月29日 下午8:48:31 
	 */
	@Override
	public List<Venue> selectByTrainTeamID(String id) {
		return venueMapper.selectByTrainTeamID(id);
	}
	
	/**  
	 * @Description: 
	 * @author 宋高俊  
	 * @param trainTeamId 培训机构ID
	 * @param id 场馆ID
	 * @return 
	 * @date 2018年10月10日 下午2:20:20 
	 */ 
	@Override
	public int deleteByTeamVenue(String trainTeamId, String id) {
		return venueMapper.deleteByTeamVenue(trainTeamId, id);
	}
	
	/**  
	 * @Description: 保存培训机构使用场地
	 * @author 宋高俊  
	 * @param id
	 * @param trainTeamId
	 * @return 
	 * @date 2018年10月10日 下午2:50:27 
	 */ 
	@Override
	public int saveTeamVenue(TrainTeamVenue trainTeamVenue) {
		return venueMapper.saveTeamVenue(trainTeamVenue);
	}

	/**  
	 * @Description: 根据培训机构统计所使用的场地
	 * @author 宋高俊  
	 * @param id
	 * @return 
	 * @date 2018年10月12日 上午9:10:10 
	 */ 
	@Override
	public int countByTeam(String id) {
		return venueMapper.countByTeam(id);
	}
	
	/**  
	 * @Description: 条件查询场地数据
	 * @author 宋高俊  
	 * @param name
	 * @param longitude
	 * @param latitude
	 * @return 
	 * @date 2018年10月13日 下午4:25:07 
	 */ 
	@Override
	public List<Venue> selectTrainVenue(String name, Double longitude, Double latitude) {
		return venueMapper.selectTrainVenue(name, longitude, latitude);
	}

	/**  
	 * @Description: 根据培训机构查询管理场馆
	 * @author 宋高俊  
	 * @param managerid
	 * @return 
	 * @date 2018年9月10日 下午4:20:09 
	 */ 
	@Override
	public List<Venue> selectByManager(String id) {
		return venueMapper.selectByManager(id);
	}

	/**  
	 * @Description: 根据名称模糊查询场地
	 * @author 宋高俊  
	 * @param name
	 * @return 
	 * @date 2018年10月18日 下午2:59:01 
	 */ 
	@Override
	public List<Venue> selectByName(String name) {
		return venueMapper.selectByName(name);
	}

	/**  
	 * @Description: 根据经纬度筛选场馆
	 * @author 宋高俊  
	 * @param begLng
	 * @param endLng
	 * @param begLat
	 * @param endLat
	 * @return 
	 * @date 2018年10月20日 下午3:25:58 
	 */ 
	@Override
	public List<Venue> selectByNearbyMapVenue(double begLng, double endLng, double begLat, double endLat, Integer ballType) {
		return venueMapper.selectByNearbyMapVenue(begLng, endLng, begLat, endLat, ballType);
	}

	/**  
	 * @Description: 条件查询场馆
	 * @author 宋高俊  
	 * @param selectType
	 * @param keyword
	 * @return 
	 * @date 2018年10月30日 上午10:09:42 
	 */ 
	@Override
	public List<Venue> selectBySearch(Integer selectType, String keyword) {
		return venueMapper.selectBySearch(selectType, keyword);
	}

	/**  
	 * @Description: 条件查询模板分析数据
	 * @author 宋高俊  
	 * @param ballType 
	 * @param trainAddFlag 
	 * @return 
	 * @date 2018年11月2日 下午8:08:47 
	 */ 
	@Override
	public List<Venue> selectByVenueSearch(String cityid, Integer sumTemplate, Integer trainAddFlag, Integer ballType) {
		return venueMapper.selectByVenueSearch(cityid, sumTemplate, trainAddFlag, ballType);
	}

	/**  
	 * @Description: 查询订单已消费可以结算的订单
	 * @author 宋高俊  
	 * @return 
	 * @date 2018年11月15日14:23:09
	 */ 
	@Override
	public List<Venue> selectByDateOut(String date) {
		return venueMapper.selectByDateOut(date);
	}

	/**  
	 * @Description: 根据手机号查询是否有匹配的场馆
	 * @author 宋高俊  
	 * @return 
	 * @date 2018年11月15日14:23:09
	 */ 
	@Override
	public List<Venue> selectByMatchingVenue(String phone) {
		return venueMapper.selectByMatchingVenue(phone);
	}

}
