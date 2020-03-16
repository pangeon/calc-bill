package pl.cecherz.calcbill.controller.web.ui_utils;

import org.springframework.web.servlet.ModelAndView;
import pl.cecherz.calcbill.repositories.OwnerRepository;
import pl.cecherz.calcbill.repositories.PaymentsRepository;

import java.util.List;

public class WebViewBuilder {
    public static ModelAndView returnOwnerListView(OwnerRepository repository) {
        ModelAndView ownerListView = new ModelAndView("owners/owners_list");
        ownerListView.addObject("owners", repository.findAll());
        return ownerListView;
    }
    public static ModelAndView returnOwnerListView(List<?> list) {
        ModelAndView ownerListView = new ModelAndView("owners/owners_payments_list");
        ownerListView.addObject("ownersPayments", list);
        return ownerListView;
    }
    public static ModelAndView returnPaymentListView(PaymentsRepository repository) {
        ModelAndView paymentsListView = new ModelAndView("payments/payments_list");
        paymentsListView.addObject("payments", repository.findAll());
        return paymentsListView;
    }
    public static ModelAndView returnPaymentListView(List<?> list) {
        ModelAndView paymentsListView = new ModelAndView("payments/payments_list");
        paymentsListView.addObject("payments", list);
        return paymentsListView;
    }
    public static ModelAndView returnPaymentItemView(Object obj) {
        ModelAndView paymentsListView = new ModelAndView("payments/edit_payment_data");
        paymentsListView.addObject("payment", obj);
        return paymentsListView;
    }
}
