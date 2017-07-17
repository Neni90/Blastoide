package com.blastoide.jsf.util;

import static com.blastoide.configuraciones.ConfiguracionesGenerales.getURL_DE_LOGO_MUNDO_LIMPIEZA;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 *
 * @author Juan Cuello
 */

public class FormatoDocumentoPresupuesto extends PdfPageEventHelper
{    
    private Image imagen;
    PdfPTable table = new PdfPTable(2);
        
    /**
     * Constructor de la clase, inicializa la imagen que se utilizara en el membrete
     */
    public FormatoDocumentoPresupuesto()
    {
        try
        {
            PdfPCell celda1 = new PdfPCell(new Phrase("Mundo Limpieza"));
            PdfPCell celda2 = new PdfPCell(new Phrase("tel: 455224/ 15537730"));
            
            imagen = Image.getInstance(getURL_DE_LOGO_MUNDO_LIMPIEZA());
            imagen.setAbsolutePosition(10, 650f);            
            
            celda1.setBorder(Rectangle.BOTTOM);
            celda2.setBorder(Rectangle.BOTTOM);
            celda2.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
            
            table.addCell(celda1);
            table.addCell(celda2);            
            
            table.setTotalWidth(350f);            
            
        }catch(Exception r)
        {
            System.err.println("Error al leer la imagen");
        }    
    }
    
    /**
     * Manejador del evento onEndPage, usado para generar el encabezado
     */
    public void onEndPage(PdfWriter writer, Document document) {

        try{            
            document.add(imagen);
            table.writeSelectedRows(0, -1, 140f, 700f, writer.getDirectContent());
            
         }catch(Exception doc)
         {
             doc.printStackTrace();
         }         
     }
}