package com.blastoide.jsfcontroller;

import com.blastoide.configuraciones.ConfiguracionesGenerales;
import com.blastoide.jpa.ClienteBueno;
import com.blastoide.jpa.FormaDePago;
import com.blastoide.jpa.FormaDePagoDAO;
import com.blastoide.jpa.TipoDeClienteDAO;
import com.blastoide.jpa.VentaDAO;
import com.blastoide.jsf.DetalleVenta;
import com.blastoide.jsf.Productos;
import com.blastoide.jsf.Venta;
import com.blastoide.jsf.util.MembretePresupuesto;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author developer
 */
@ManagedBean
@ViewScoped
public class VentaBean extends ConfiguracionesGenerales implements Serializable{

    private Venta venta = new Venta();
    private Productos producto = new Productos();
    private int cantidad;
    private List<DetalleVenta> lista = new ArrayList();
    
    private int formaDePagoID = 1;

    
    public int getFormaDePagoID() {
        return formaDePagoID;
    }

    public void setFormaDePagoID(int formaDePagoID) {
        this.formaDePagoID = formaDePagoID;
    }
    
    
    
    
    public List<DetalleVenta> getLista() {
        return lista;
    }

    public void setLista(List<DetalleVenta> lista) {
        this.lista = lista;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    
    
    public void agregar() throws Exception {

        
        
        FormaDePagoDAO formapagoDao = new FormaDePagoDAO();
        Double porcentaje;
        porcentaje = formapagoDao.buscarPorcentaje(formaDePagoID);
        System.err.println("porcentaje por Forma De Pago: "+porcentaje);

        
        
        TipoDeClienteDAO tipoClienteDao = new TipoDeClienteDAO();
        Double porcentajePorTipoDeCliente;
        porcentajePorTipoDeCliente = tipoClienteDao.buscarPorcentajeDeTipoDeCLiente(venta.getCliente().getTipoCliente());
        System.err.println("porcentajePor Tipo De Cliente: "+porcentajePorTipoDeCliente);
                
                
        DetalleVenta det = new DetalleVenta();
        
        det.setCantidad(cantidad);
        
        
        Double precioUnitario = producto.getPrecioVenta();
                    System.err.println("Producto precio venta unitario: "+producto.getPrecioVenta());
                    
                    

        double precioDePorcentajeASumar;
        double precioDePorcentajePorTipoDeClienteASumar;
            
        
            precioDePorcentajeASumar = precioUnitario*porcentaje /100.0;
            System.err.println("precio De Porcentaje A Sumar: "+precioDePorcentajeASumar);

            
            precioDePorcentajePorTipoDeClienteASumar = precioUnitario*porcentajePorTipoDeCliente /100.0;
            
            
            
            
           Double PrecioTotal = precioUnitario + precioDePorcentajeASumar - precioDePorcentajePorTipoDeClienteASumar;
            System.err.println("PrecioTotal: "+PrecioTotal);

           
        producto.setPrecioFinalAFacturar(PrecioTotal);
                    
        det.setProducto(producto);
        
        this.lista.add(det);
        
        
    }

    
    
/**
 *
 *  
 * monto = es el total del precio de todos ya con los intereses sumados de "tipo de cliente" y los intereses sumados por "forma de pago" los productos en la venta por su cantidad.
 * también faltaría por "condición de IVa " que es 0 por ahora por eso no fué hecho todavía.
 */
    public void facturar() throws Exception {

        VentaDAO dao;
        FormaDePagoDAO formapagoDao = new FormaDePagoDAO();
        
        
        
        double monto = 0;
        try {

            for (DetalleVenta det : lista) {
                monto += det.getProducto().getPrecioVenta() * det.getCantidad();
                    
            }
            
            dao = new VentaDAO();
            venta.setMonto(monto);
            venta.setFecha(Calendar.getInstance().getTime());
            venta.setFormadePagoID(formaDePagoID);
            dao.registrar(venta, lista);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Venta Factururada exitosamente"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No se pudo realizar la facturación"));
        } finally {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        }

    }

    public void presupuestar() throws FileNotFoundException, DocumentException, IOException {

        double monto = 0;

        for (DetalleVenta det : lista) {
            System.err.println("det: "+det.toString());
            
            
            monto += det.getProducto().getPrecioFinalAFacturar() * det.getCantidad();
            
        }

        venta.setMonto(monto);
        venta.setFecha(Calendar.getInstance().getTime());
        
         MembretePresupuesto doc = new MembretePresupuesto();
            doc.createPdf("documento.pdf",lista,venta);
        
        

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("PDF generado"));

    }

    
 /*   public Map<String, Map<String, String>> getData() {
        return data;
    }
 
    TRABAJAR CON ESTO
    https://www.primefaces.org/showcase/ui/ajax/dropdown.xhtml
    
    public void cuandoClienteCambie() {
        if(this.venta.getCliente() !=null )
            cities = data.get(country);
        else
            cities = new HashMap<String, String>();
    }
   */ 
    
    
    
}
