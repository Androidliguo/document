package com.lg.document.dao;


import java.util.List;

import com.lg.document.model.Document;

public interface IDocumentDao extends IBaseDao<Document> {
	/**
	 * 找到某个人所接收到的所有的公文
	 * @param userId
	 * @return
	 */
	public List<Document> findAllReceiveDoc(int userId);
}