package com.juheshi.video.common.wechat.resp;

import java.util.List;

/***
 * Create date:2015-01-19
 * <p>
 * 图文消息
 * </p>
 * <hr>
 * Edit List:
 * <hr>
 * @author 马跃
 * 
 */
public class NewsMessage extends BaseMessage {

	//图文消息个数，限制为8条以内
	private int ArticleCount = 1;
	/**
	 * 多条图文消息信息，默认第一个item为大图
	 */
	private List<Article> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		Articles = articles;
	}
}