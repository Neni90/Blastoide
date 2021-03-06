package com.blastoide.jsfcontroller;

import com.blastoide.jsf.ProductoXProveedor;
import com.blastoide.jsfcontroller.util.JsfUtil;
import com.blastoide.jsfcontroller.util.JsfUtil.PersistAction;
import com.blastoide.jpa.ProductoXProveedorFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("productoXProveedorController")
@SessionScoped
public class ProductoXProveedorController implements Serializable {

    @EJB
    private com.blastoide.jpa.ProductoXProveedorFacade ejbFacade;
    private List<ProductoXProveedor> items = null;
    private ProductoXProveedor selected;

    public ProductoXProveedorController() {
    }

    public ProductoXProveedor getSelected() {
        return selected;
    }

    public void setSelected(ProductoXProveedor selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getProductoXProveedorPK().setProductoID(selected.getProductos().getProductoID());
        selected.getProductoXProveedorPK().setProveedorID(selected.getProveedores().getProveedorID());
    }

    protected void initializeEmbeddableKey() {
        selected.setProductoXProveedorPK(new com.blastoide.jsf.ProductoXProveedorPK());
    }

    private ProductoXProveedorFacade getFacade() {
        return ejbFacade;
    }

    public ProductoXProveedor prepareCreate() {
        selected = new ProductoXProveedor();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ProductoXProveedorCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProductoXProveedorUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ProductoXProveedorDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ProductoXProveedor> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public ProductoXProveedor getProductoXProveedor(com.blastoide.jsf.ProductoXProveedorPK id) {
        return getFacade().find(id);
    }

    public List<ProductoXProveedor> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ProductoXProveedor> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = ProductoXProveedor.class)
    public static class ProductoXProveedorControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductoXProveedorController controller = (ProductoXProveedorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productoXProveedorController");
            return controller.getProductoXProveedor(getKey(value));
        }

        com.blastoide.jsf.ProductoXProveedorPK getKey(String value) {
            com.blastoide.jsf.ProductoXProveedorPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new com.blastoide.jsf.ProductoXProveedorPK();
            key.setProveedorID(Integer.parseInt(values[0]));
            key.setProductoID(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(com.blastoide.jsf.ProductoXProveedorPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getProveedorID());
            sb.append(SEPARATOR);
            sb.append(value.getProductoID());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ProductoXProveedor) {
                ProductoXProveedor o = (ProductoXProveedor) object;
                return getStringKey(o.getProductoXProveedorPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ProductoXProveedor.class.getName()});
                return null;
            }
        }

    }

}
