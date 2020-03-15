package pl.cecherz.calcbill.controller.web.ui_utils;

import org.springframework.web.servlet.ModelAndView;
import pl.cecherz.calcbill.repositories.OwnerRepository;

public class WebViewBuilder {
    public static ModelAndView returnOwnerListView(OwnerRepository repository) {
        ModelAndView ownerListView = new ModelAndView("owners/owners_list");
        ownerListView.addObject("owners", repository.findAll());
        return ownerListView;
    }
}
