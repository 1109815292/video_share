package com.juheshi.video.common.wechat.resp;

/***
 * Create date:2015-01-19
 * <p>
 * 文本消息
 * </p>
 * <hr>
 * Edit List:
 * <hr>
 * @author 马跃
 * 
 */
public class TextMessage extends BaseMessage {
	// 回复的消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}