/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bodcol.utilitarios;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Named(value = "jasperReportUtil")
@ApplicationScoped
public class JasperReportUtil {

    //esto es igual al de la conexxion en el widfly
    @Resource(lookup = "java:/BodegaDS")
    private DataSource BodegaDS;

    public JasperReportUtil() {
    }

    private void export(final String nombreReporte, TipoReporte tipoReporte, Map<String, Object> params, Object dataSource) throws Exception {
        FacesContext fContext = FacesContext.getCurrentInstance();
        ExternalContext eContext = fContext.getExternalContext();
        ServletContext sContext = (ServletContext) eContext.getContext();
        HttpServletResponse response = (HttpServletResponse) eContext.getResponse();

        
        //se coloca la carpeta donde estan los compilados
        String pathReportes = sContext.getRealPath("reporte");

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("SUBREPORT_DIR", pathReportes);
        if (params != null) {
            parametros.putAll(params);
        }
        JasperPrint jasperPrint = null;
        InputStream inputStream = new FileInputStream(pathReportes + File.separator + nombreReporte + ".jasper");
        if (dataSource instanceof Connection) {
            jasperPrint = JasperFillManager.fillReport(inputStream, parametros, (Connection) dataSource);
        } else {
            jasperPrint = JasperFillManager.fillReport(inputStream, parametros, (JRBeanCollectionDataSource) dataSource);
        }

        if (tipoReporte.equals(TipoReporte.PDF)) {
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            response.setContentType("application/pdf");
        } else if (tipoReporte.equals(TipoReporte.XLSX)) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment; filename=" + nombreReporte + ".xlsx");
            response.setContentLength(os.size());
            response.getOutputStream().write(os.toByteArray());
        }
        fContext.responseComplete();
    }

    public void exportToPdf(final String nombreReporte, Map<String, Object> params) throws Exception {
        try (Connection conn = BodegaDS.getConnection()) {
            export(nombreReporte, TipoReporte.PDF, params, conn);
        }
    }

    public void exportToPdf(final String nombreReporte, Map<String, Object> params, Collection<?> objectList) throws Exception {
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(objectList);
        export(nombreReporte, TipoReporte.PDF, params, ds);
    }

    public void exportToXlsx(final String nombreReporte, Map<String, Object> params) throws Exception {
        try (Connection conn = BodegaDS.getConnection()) {
            export(nombreReporte, TipoReporte.XLSX, params, conn);
        }
    }

    public void exportToXlsx(final String nombreReporte, Map<String, Object> params, Collection<?> objectList) throws Exception {
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(objectList);
        export(nombreReporte, TipoReporte.XLSX, params, ds);
    }

}
