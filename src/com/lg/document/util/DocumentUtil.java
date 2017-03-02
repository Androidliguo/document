package com.lg.document.util;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import com.lg.document.dao.IAttachmentDao;
import com.lg.document.dto.AttachDto;
import com.lg.document.model.Attachment;
import com.lg.document.model.Document;
import com.lg.document.model.Message;
//数据传输对象dto.这个对象是不存储在数据库中的，这只是起传递数据的作用。
public class DocumentUtil {
	public static String[] addAttach(AttachDto ad,IAttachmentDao attachmentDao,Message msg, Document doc) throws IOException {
		// 1、添加私人信件对象
		/**
		 * 这里的判断只是为了提高运行的效率而已。
		 * 如果没有附件要上传的话，那么直接就返回null就可以了。
		 */
		if (ad.isHasAttach()) {
			// 需要添加附件
			File[] atts = ad.getAtts();
			String[] attsContentType = ad.getAttsContentType();
			String[] attsFilename = ad.getAttsFileName();
			String[] newNames = new String[atts.length];
			for (int i = 0; i < atts.length; i++) {
				File f = atts[i];
				String fn = attsFilename[i];
				String contentType = attsContentType[i];
				Attachment a = new Attachment();
				a.setContentType(contentType);
				a.setCreateDate(new Date());
				//私人信息msg和公文doc都是有可能有附件的。
				if(msg!=null)
					a.setMessage(msg);
				if(doc!=null) a.setDocument(doc);
				a.setOldName(fn);
				a.setSize(f.length());
				a.setType(FilenameUtils.getExtension(fn));
				String newName = getNewName(fn);
				a.setNewName(newName);
				newNames[i] = newName;
				attachmentDao.add(a);
			}
			// 上传附件
			String uploadPath = ad.getUploadPath();
			for (int i = 0; i < atts.length; i++) {
				File f = atts[i];
				String n = newNames[i];
				String path = uploadPath + "/" + n;
				FileUtils.copyFile(f, new File(path));
			}
			return newNames;
		}
		return null;
	}
	
	private static String getNewName(String name) {
		String n = new Long(new Date().getTime()).toString();
		n = n+"."+FilenameUtils.getExtension(name);
		return n;
	}
}

