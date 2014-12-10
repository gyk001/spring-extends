package cn.guoyukun.spring.ext;

import java.util.Properties;
import java.util.logging.Logger;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ProfilePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer{
	//日志对象
	private static final Logger LOG = Logger.getLogger(ProfilePropertyPlaceholderConfigurer.class.getName());

	private static final String DEFAULT_PROFILE_PLACEHOLDER = "profile";
	private static final String DEFAULT_PROFILE_CONFIG_PATH = "/profile.properties";
	private static final String DEFAULT_PROFILE = "default";
	
	private String profilePlaceholder = DEFAULT_PROFILE_PLACEHOLDER;
	
	private String profileConfigPath = DEFAULT_PROFILE_CONFIG_PATH;
	
	private String profile;
	
	private void loadConfig(){
		if(profile!=null){
			return;
		}
		Properties p = new Properties();
		try {
			p.load(ProfilePropertyPlaceholderConfigurer.class.getResourceAsStream(profileConfigPath));
			profile = p.getProperty(profilePlaceholder,DEFAULT_PROFILE);
		} catch (Exception e) {
			throw new RuntimeException("加载["+profileConfigPath+"]失败",e);
		}
	}
	
	public void setLocations(Resource[] locations) {
		loadConfig();
		LOG.info("加载["+profile+"]下的配置文件");
		if(locations !=null ){
			Resource[] newRes = new Resource[locations.length];
			for(int i=0; i< locations.length; i++){
				Resource r = locations[i];
				if(r!=null && r instanceof ClassPathResource){
					String path = ((ClassPathResource)r).getPath();
					if(path.contains("${"+profilePlaceholder+"}")){
						path = path.replace("${"+profilePlaceholder+"}", profile);
						newRes[i]=new ClassPathResource(path);
					}else{
						newRes[i]=r;
					}
				}else{
					newRes[i]=r;
				}
			}
			super.setLocations( newRes );
		}else{
			super.setLocations( null ) ;
		}
	}
	
}
