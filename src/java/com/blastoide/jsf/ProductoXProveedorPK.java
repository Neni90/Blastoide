/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blastoide.jsf;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author developer
 */
@Embeddable
public class ProductoXProveedorPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ProveedorID")
    private int proveedorID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ProductoID")
    private int productoID;

    public ProductoXProveedorPK() {
    }

    public ProductoXProveedorPK(int proveedorID, int productoID) {
        this.proveedorID = proveedorID;
        this.productoID = productoID;
    }

    public int getProveedorID() {
        return proveedorID;
    }

    public void setProveedorID(int proveedorID) {
        this.proveedorID = proveedorID;
    }

    public int getProductoID() {
        return productoID;
    }

    public void setProductoID(int productoID) {
        this.productoID = productoID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) proveedorID;
        hash += (int) productoID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoXProveedorPK)) {
            return false;
        }
        ProductoXProveedorPK other = (ProductoXProveedorPK) object;
        if (this.proveedorID != other.proveedorID) {
            return false;
        }
        if (this.productoID != other.productoID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.blastoide.jsf.ProductoXProveedorPK[ proveedorID=" + proveedorID + ", productoID=" + productoID + " ]";
    }
    
}
