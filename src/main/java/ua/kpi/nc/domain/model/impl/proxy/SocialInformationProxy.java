package ua.kpi.nc.domain.model.impl.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import ua.kpi.nc.domain.model.SocialInformation;
import ua.kpi.nc.domain.model.SocialNetwork;
import ua.kpi.nc.domain.model.User;
import ua.kpi.nc.domain.model.impl.real.SocialInformationImpl;
import ua.kpi.nc.service.SocialInformationService;

/**
 * Created by Chalienko on 15.04.2016.
 */
@Configurable
public class SocialInformationProxy implements SocialInformation {
    private Long id;

    private SocialInformationImpl socialInformation;

    @Autowired
    private SocialInformationService socialInformationService;

    public SocialInformationProxy(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getAccessInfo() {
        if (socialInformation == null) {
            socialInformation = downloadSocialInformation();
        }
        return socialInformation.getAccessInfo();
    }

    @Override
    public void setAccessInfo(String accessInfo) {
        if (socialInformation == null) {
            socialInformation = downloadSocialInformation();
        }
        socialInformation.setAccessInfo(accessInfo);
    }

    @Override
    public User getUser() {
        if (socialInformation == null) {
            socialInformation = downloadSocialInformation();
        }
        return socialInformation.getUser();
    }

    @Override
    public void setUser(User user) {
        if (socialInformation == null) {
            socialInformation = downloadSocialInformation();
        }
        socialInformation.setUser(user);
    }

    @Override
    public SocialNetwork getSocialNetwork() {
        if (socialInformation == null) {
            socialInformation = downloadSocialInformation();
        }
        return socialInformation.getSocialNetwork();
    }

    @Override
    public void setSocialNetwork(SocialNetwork socialNetwork) {

    }

    private SocialInformationImpl downloadSocialInformation() {
        return (SocialInformationImpl) socialInformationService.getById(id);
    }
}
