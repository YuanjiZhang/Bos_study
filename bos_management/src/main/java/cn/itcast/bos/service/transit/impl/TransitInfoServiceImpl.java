package cn.itcast.bos.service.transit.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.transit.TransitInfoService;
@Service
@Transactional
public class TransitInfoServiceImpl implements TransitInfoService {

	@Autowired
	private TransitInfoRepository transitInfoRepository;
	@Autowired
	private WayBillRepository wayBillRepository;
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;
	
	
	@Override
	public void createTransits(String wayBillIds) {
		if(StringUtils.isNotBlank(wayBillIds)){
			//处理运单
			for (String wayBillId : wayBillIds.split(",")) {
				WayBill wayBill = wayBillRepository.findOne(Integer.parseInt(wayBillId));
				//判断运单状态  是否为待发货
				if(wayBill.getSignStatus() == 1){
					//待发货
					//生成TransitInfo信息
					TransitInfo transitInfo = new TransitInfo();
					transitInfo.setWayBill(wayBill);
					transitInfo.setStatus("出入库中转");
					transitInfoRepository.save(transitInfo);
					
					//更改运单状态  2:派送中
					wayBill.setSignStatus(2);
					
					//同步索引库
					wayBillRepository.save(wayBill);
				}
			}
		}
		
	}

	@Override
	public Page<TransitInfo> findPageData(Pageable pageable) {
		return transitInfoRepository.findAll(pageable);
	}


}
