package pl.cecherz.calcbill.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.cecherz.calcbill.controller.db.OwnerController;
import pl.cecherz.calcbill.controller.db.PaymentsController;
import pl.cecherz.calcbill.exeptions.EmptyFindResultException;
import pl.cecherz.calcbill.exeptions.EntityNotFoundException;
import pl.cecherz.calcbill.model.db.Owner;
import pl.cecherz.calcbill.model.db.Payments;

import java.util.List;

@Controller("OwnerConsumer")
@RequestMapping("/web")
public class ThymeleafBuilder {

    private final OwnerController ownerController;
    private final PaymentsController paymentsController;

    public ThymeleafBuilder(OwnerController ownerController, PaymentsController paymentsController) {
        this.ownerController = ownerController;
        this.paymentsController = paymentsController;
    }
    /* ------------------------- Metody GET --------------------- */
        @RequestMapping(method = RequestMethod.GET)
        public ModelAndView startWeb() {
            return new ModelAndView("builder-test");
        }
        @RequestMapping(value="/add-owner", method = RequestMethod.GET)
        public ModelAndView redirectToOwnerAddForm() {
            return new ModelAndView("add-owner-form", "owner", new Owner());
        }
        @RequestMapping(value="/add-payment/{id}", method = RequestMethod.GET)
        public ModelAndView redirectToPaymentAddForm(@PathVariable Integer id) {
            ModelAndView view = new ModelAndView("add-payment-form");
            view.addObject("owner", ownerController.getOwner(id));
            view.addObject("payment", ownerController.getOwnerPayments(id));
            return view;
        }
        @RequestMapping(value = "/owners", method = RequestMethod.GET)
        public ModelAndView forwardOwnersListToWeb() {
            return returnListView("builder-test", "owners", ownerController.getAllOwners());
        }
        @RequestMapping(value = "/payments", method = RequestMethod.GET)
        public ModelAndView forwardPaymentsListToWeb() {
            return returnListView("builder-test", "payments", paymentsController.getAllPayments());
        }
        @RequestMapping(value = "/owner/{id}", method = RequestMethod.GET)
        public ModelAndView forwardOwnerToWeb(@PathVariable Integer id) {
            try {
                return returnItemView("builder-test", "owner", ownerController.getOwner(id));
            } catch (EntityNotFoundException e) {
                return new ModelAndView("error-entity-not-found");
            }
        }
        @RequestMapping(value = "/payments/{id}", method = RequestMethod.GET)
        public ModelAndView forwardPaymentToWeb(@PathVariable Integer id) {
            try {
                return returnItemView("builder-test", "payment", paymentsController.getPayment(id));
            } catch (EntityNotFoundException e) {
                return new ModelAndView("error-entity-not-found");
            }
        }
        @RequestMapping(value = "/owner-payments/{id}", method = RequestMethod.GET)
        public ModelAndView forwardOwnerPaymetsListToWeb(@PathVariable Integer id) {
            try {
                return returnItemView("builder-test", "ownerPayments", ownerController.getOwnerPayments(id));
            } catch (EntityNotFoundException e) {
                return new ModelAndView("error-entity-not-found");
            }
        }
        @RequestMapping(value = "/owner-payments/{id}/{kind}", method = RequestMethod.GET)
        public ModelAndView forwardOwnerPaymetsListByKindToWeb(@PathVariable Integer id, @PathVariable String kind) {
            try {
                return returnItemView("builder-test", "ownerPaymentsByKind", ownerController.getOwnerPaymentsByKind(id, kind));
            } catch(EmptyFindResultException e) {
                return new ModelAndView("error-empty-find");
            } catch (EntityNotFoundException e) {
                return new ModelAndView("error-entity-not-found");
            }
        }
        @RequestMapping(value = "/payments-filter/{kind}", method = RequestMethod.GET)
        public ModelAndView forwardPaymetsListByKindToWeb(@PathVariable String kind) {
            try {
                return returnItemView("builder-test", "paymentsByKind", paymentsController.filterPaymentsByKind(kind));
            } catch(EmptyFindResultException e) {
                return new ModelAndView("error-empty-find");
            }
        }
        @RequestMapping(value = "/owner-payments/{id}/{min}/{max}", method = RequestMethod.GET)
        public ModelAndView forwardOwnerPaymetsListByAmountToWeb(@PathVariable Integer id, @PathVariable Double min,  @PathVariable Double max) {
            try {
                return returnItemView("builder-test", "ownerPaymentsByAmount", ownerController.getOwnerPaymentsByAmonutRange(id, min, max));
            } catch (EmptyFindResultException e) {
                return new ModelAndView("error-empty-find");
            } catch (EntityNotFoundException e) {
                return new ModelAndView("error-entity-not-found");
            }
        }
        @RequestMapping(value = "/payments-filter/{min}/{max}", method = RequestMethod.GET)
        public ModelAndView forwardPaymetsListByAmountToWeb(@PathVariable Double min, @PathVariable Double max) {
            try {
                return returnItemView("builder-test", "paymentsByAmount", paymentsController.filterPaymentsByAmonutRange(min, max));
            } catch(EmptyFindResultException e) {
                return new ModelAndView("error-empty-find");
            }
        }
        @RequestMapping(value = "/sum-owner-payments/{id}", method = RequestMethod.GET)
        public ModelAndView forwardOwnerPaymetsSumToWeb(@PathVariable Integer id) {
            try {
                return returnItemView("builder-test", "sumOwnerPayments", ownerController.getSumOwnerPayments(id));
            } catch (EntityNotFoundException e) {
                return new ModelAndView("error-entity-not-found");
            }
        }
        @RequestMapping(value = "/sum-owner-payments/{id}/{kind}", method = RequestMethod.GET)
        public ModelAndView forwardOwnerPaymetsSumByKindToWeb(@PathVariable Integer id, @PathVariable String kind) {
            try {
                return returnItemView("builder-test", "sumOwnerPaymentsByKind", ownerController.getSumOwnerPaymentsByKind(id, kind));
            } catch(EmptyFindResultException e) {
                return new ModelAndView("error-empty-find");
            } catch (EntityNotFoundException e){
                return new ModelAndView("error-entity-not-found");
            }
        }
    /* ------------------------- Metody GET --------------------- */

    /* ------------------------- Metody POST --------------------- */
    @RequestMapping(value = "/owners", method = RequestMethod.POST)
    public ModelAndView forwardAddOwnerToWeb(Owner owner) {
        ownerController.addOwner(owner);
        return returnListView("builder-test", "owners", ownerController.getAllOwners());
    }
    @RequestMapping(value = "/payments/{id}", method = RequestMethod.POST)
    public ModelAndView forwardAddOwnerPaymentToWeb(Payments payment, @PathVariable Integer id) {
        paymentsController.addPayment(payment, id);
        return returnListView("builder-test", "owners", ownerController.getAllOwners());
    }
    /* ------------------------- Metody POST --------------------- */

    /* ------------------------- Metody prywatne --------------------- */
        private ModelAndView returnListView(String viewName, String attributeName, List<?> list) {
            return new ModelAndView(viewName).addObject(attributeName, list);
        }
        private ModelAndView returnItemView(String viewName, String attributeName, Object item) {
            return new ModelAndView(viewName).addObject(attributeName, item);
        }
    /* ------------------------- Metody prywatne --------------------- */
}
