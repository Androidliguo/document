package com.lg.document.dao;


import org.springframework.stereotype.Repository;

import com.lg.document.model.Attachment;

@Repository("attachmentDao")
public class AttachmentDao extends BaseDao<Attachment> implements IAttachmentDao {
	
}
