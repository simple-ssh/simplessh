package simplessh.com.dao;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;

import java.io.InputStream;

/**
 * @author Corneli F.
 */
public class DownloadFile {
    private Channel channelDownload;
    private ChannelSftp channelSftpDownload;
    private InputStream file;

    public DownloadFile() {
    }

    public DownloadFile(Channel channelDownload, ChannelSftp channelSftpDownload, InputStream file) {
        this.channelDownload = channelDownload;
        this.channelSftpDownload = channelSftpDownload;
        this.file = file;
    }

    public Channel getChannelDownload() {
        return channelDownload;
    }

    public void setChannelDownload(Channel channelDownload) {
        this.channelDownload = channelDownload;
    }

    public ChannelSftp getChannelSftpDownload() {
        return channelSftpDownload;
    }

    public void setChannelSftpDownload(ChannelSftp channelSftpDownload) {
        this.channelSftpDownload = channelSftpDownload;
    }

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }
}
