package cn.itcast.bos.dao.transit;


import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.transit.DeliveryInfo;

public interface DeliberyInfoRepository extends JpaRepository<DeliveryInfo, Integer> {

}
