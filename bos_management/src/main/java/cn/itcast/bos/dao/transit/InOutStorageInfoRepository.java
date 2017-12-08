package cn.itcast.bos.dao.transit;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.transit.InOutStorageInfo;

public interface InOutStorageInfoRepository extends JpaRepository<InOutStorageInfo, Integer>{

}
