package pl.cecherz.calcbill.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.cecherz.calcbill.controller.db.OwnerController;
import pl.cecherz.calcbill.controller.db.PaymentsController;
import pl.cecherz.calcbill.exeptions.EmptyFindResultException;
import pl.cecherz.calcbill.exeptions.EntityEmptyContentException;
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
    /* ------------------------- Przekierowania --------------------- */
        @RequestMapping(method = RequestMethod.GET)
        public ModelAndView startWeb() {
            return new ModelAndView("html-view-manager");
        }
        @RequestMapping(value="/redirect-to-add-owner", method = RequestMethod.GET)
        public ModelAndView redirectToOwnerAddForm() {
            return new ModelAndView("add-owner-form", "owner", new Owner());
        }
        @RequestMapping(value="/redirect-to-edit-owner/{id}", method = RequestMethod.GET)
        public ModelAndView redirectToOwnerEditForm(@PathVariable Integer id) {
            return new ModelAndView("edit-owner-form", "owner", ownerController.getOwner(id));
        }
        @RequestMapping(value="/redirect-to-add-payment/{id}", method = RequestMethod.GET)
        public ModelAndView redirectToPaymentAddForm(@PathVariable Integer id) {
            ModelAndView view = new ModelAndView("add-payment-form");
            view.addObject("owner", ownerController.getOwner(id));
            view.addObject("payment", new Payments());
            return view;
        }
        @RequestMapping(value="/redirect-to-edit-payment/{id}", method = RequestMethod.GET)
        public ModelAndView redirectToPaymentEditForm(@PathVariable Integer id) {
            ModelAndView view = new ModelAndView("edit-payment-form");
            //view.addObject("owner", ownerController.getOwner(id));
            view.addObject("payment", paymentsController.getPayment(id));
            return view;
        }
    /* ------------------------- Przekierowania --------------------- */

    /* ------------------------- Metody GET --------------------- */

        @RequestMapping(value = "/owners", method = RequestMethod.GET)
        public ModelAndView forwardOwnersListToWeb() {
            return returnListView("html-view-manager", "owners", ownerController.getAllOwners());
        }
        @RequestMapping(value = "/payments", method = RequestMethod.GET)
        public ModelAndView forwardPaymentsListToWeb() {
            return returnListView("html-view-manager", "payments", paymentsController.getAllPayments());
        }
        @RequestMapping(value = "/owner/{id}", method = RequestMethod.GET)
        public ModelAndView forwardOwnerToWeb(@PathVariable Integer id) {
            try {
                return returnItemView("html-view-manager", "owner", ownerController.getOwner(id));
            } catch (EntityNotFoundException e) {
                return new ModelAndView("error-entity-not-found");
            }
        }
        @RequestMapping(value = "/payments/{id}", method = RequestMethod.GET)
        public ModelAndView forwardPaymentToWeb(@PathVariable Integer id) {
            try {
                return returnItemView("html-view-manager", "payment", paymentsController.getPayment(id));
            } catch (EntityNotFoundException e) {
                return new ModelAndView("error-entity-not-found");
            }
        }
        @RequestMapping(value = "/owner-payments/{id}", method = RequestMethod.GET)
        public ModelAndView forwardOwnerPaymetsListToWeb(@PathVariable Integer id) {
            try {
                ModelAndView view = new ModelAndView("html-view-manager");
                view.addObject("sum", ownerController.getSumOwnerPayments(id));
                view.addObject("ownerPayments", ownerController.getOwnerPayments(id));
                return view;
            } catch(EntityEmptyContentException e) {
                return new ModelAndView("error-empty-content");
            } catch (EntityNotFoundException e) {
                return new ModelAndView("error-entity-not-found");
            }
        }
        @RequestMapping(value = "/payments-filter-by-kind", method = RequestMethod.GET)
        public ModelAndView forwardPaymetsListByKindToWeb(String kind) {
            try {
                return returnItemView("html-view-manager", "payments", paymentsController.filterPaymentsByKind(kind));
            } catch(EmptyFindResultException e) {
                return new ModelAndView("error-empty-find");
            }
        }
        @RequestMapping(value = "/payments-filter-by-amount", method = RequestMethod.GET)
        public ModelAndView forwardPaymetsListByAmountToWeb(Double min, Double max) {
            try {
                return returnItemView("html-view-manager", "payments", paymentsController.filterPaymentsByAmonutRange(min, max));
            } catch(EmptyFindResultException e) {
                return new ModelAndView("error-empty-find");
            }
        }
        @RequestMapping(value = "/sum-owner-payments/{id}", method = RequestMethod.GET)
        public ModelAndView forwardOwnerPaymetsSumToWeb(@PathVariable Integer id) {
            try {
                return returnItemView("html-view-manager", "sumOwnerPayments", ownerController.getSumOwnerPayments(id));
            } catch (EntityNotFoundException e) {
                return new ModelAndView("error-entity-not-found");
            }
        }
    /* ------------------------- Metody GET --------------------- */

    /* ------------------------- Metody POST --------------------- */
        @RequestMapping(value = "/owners", method = RequestMethod.POST)
        public ModelAndView forwardAddOwnerToWeb(Owner owner) {
            ownerController.addOwner(owner);
            return returnListView("html-view-manager", "owners", ownerController.getAllOwners());
        }
        @RequestMapping(value = "/payments/{id}", method = RequestMethod.POST)
        public ModelAndView forwardAddOwnerPaymentToWeb(Payments payment, @PathVariable Integer id) {
            paymentsController.addPayment(payment, id);
            return new ModelAndView("success");
        }
    /* ------------------------- Metody POST --------------------- */
    /* ------------------------- Metody PUT --------------------- */
        @RequestMapping(value = "/edit-owner/{id}", method = RequestMethod.POST)
        public ModelAndView forwardreplaceOwnerToWeb(@PathVariable Integer id, Owner owner) {
            ownerController.replaceOwner(id, owner);
            return returnListView("html-view-manager", "owners", ownerController.getAllOwners());
        }
        @RequestMapping(value = "/edit-payment/{id}", method = RequestMethod.POST)
        public ModelAndView forwardreplaceOwnerToWeb(@PathVariable Integer id, Payments payment) {
            paymentsController.replacePayment(id, payment);
            ModelAndView view = new ModelAndView("builder-test");
            return new ModelAndView("success");
        }
    /* ------------------------- Metody PUT --------------------- */
    /* ------------------------- Metody DELETE --------------------- */
        @RequestMapping(value = "/delete-owner/{id}", method = RequestMethod.GET)
        public ModelAndView forwardDeleteOwnerToWeb(@PathVariable Integer id) {
            ownerController.deleteOwner(id);
            return returnListView("html-view-manager", "owners", ownerController.getAllOwners());
        }
        @RequestMapping(value = "/delete-payment/{id}", method = RequestMethod.GET)
        public ModelAndView forwardDeletePaymentToWeb(@PathVariable Integer id) {
            paymentsController.deletePayment(id);
            return returnListView("html-view-manager", "owners", ownerController.getAllOwners());
        }
    /* ------------------------- Metody DELETE --------------------- */

    /* ------------------------- Metody prywatne --------------------- */
        private ModelAndView returnListView(String viewName, String attributeName, List<?> list) {
            return new ModelAndView(viewName).addObject(attributeName, list);
        }
        private ModelAndView returnItemView(String viewName, String attributeName, Object item) {
            return new ModelAndView(viewName).addObject(attributeName, item);
        }
    /* ------------------------- Metody prywatne --------------------- */
}
