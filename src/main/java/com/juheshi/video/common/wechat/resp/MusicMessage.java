package com.juheshi.video.common.wechat.resp;

/***
 * Create date:2015-01-19
 * <p>
 * 音乐消息
 * </p>
 * <hr>
 * Edit List:
 * <hr>
 * @author 马跃
 * 
 */
public class MusicMessage extends BaseMessage {
	// 音乐
	private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}
}