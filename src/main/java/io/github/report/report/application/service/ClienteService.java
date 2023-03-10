package io.github.report.report.application.service;

import io.github.report.report.application.entities.Cliente;
import io.github.report.report.application.repository.ClienteRepository;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> findAll() {
        return repository.findAll();
    }

    public Cliente create(Cliente cliente) {
        return repository.save(cliente);
    }

    public void exportReport(String reportFormat, HttpServletResponse response) throws IOException, JRException {


        String PATH = "/home/desenvolvimento/Documentos/Projects/Relatorios";


        List<Cliente> clientes = repository.findAll();


        File file = ResourceUtils.getFile("classpath:baseReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(clientes);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Carol Techie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            response.setContentType("text/html");
            // Define que o arquivo pode ser visualizado no navegador e tamb??m nome final do arquivo
            // para fazer download do relat??rio troque 'inline' por 'attachment'
            response.setHeader("Content-Disposition", "attachment; filename=relatorio.html");
             JasperExportManager.exportReportToHtmlFile(jasperPrint, PATH + "\\clientes.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            response.setContentType("application/pdf");
            // Define que o arquivo pode ser visualizado no navegador e tamb??m nome final do arquivo
            // para fazer download do relat??rio troque 'inline' por 'attachment'
            response.setHeader("Content-Disposition", "attachment; filename=livros.pdf");
            JasperExportManager.exportReportToPdfFile(jasperPrint, PATH + "\\relatorio.pdf");
        }

        final OutputStream outStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
    }
}


/*
*
* O m??todo exportReport tem como objetivo gerar relat??rios para uma lista de clientes, no formato escolhido pelo usu??rio (PDF ou HTML). Ele recebe como par??metro uma string reportFormat, que indica qual formato deve ser utilizado.
    O primeiro passo do m??todo ?? definir o caminho onde o arquivo ser?? salvo, atrav??s da vari??vel PATH. Em seguida, ?? feita a consulta dos clientes no reposit??rio, atrav??s do m??todo findAll(), e os resultados s??o armazenados na lista clientes.
    O pr??ximo passo ?? carregar o arquivo clientes.jrxml, que cont??m o modelo do relat??rio, utilizando a classe ResourceUtils e compil??-lo atrav??s da classe JasperCompileManager. O resultado ?? armazenado na vari??vel jasperReport.
    Os dados dos clientes s??o passados para o relat??rio atrav??s de um objeto JRBeanCollectionDataSource, que ?? criado a partir da lista clientes.
    Um mapa de par??metros ?? criado para ser utilizado pelo relat??rio, que neste caso cont??m apenas a chave "createdBy" e o valor "Java Techie".
    O relat??rio ?? preenchido com os dados dos clientes utilizando a classe JasperFillManager e o resultado ?? armazenado na vari??vel jasperPrint.
    Se o formato do relat??rio escolhido pelo usu??rio for "html", o m??todo JasperExportManager.exportReportToHtmlFile ?? chamado para exportar o relat??rio em formato HTML para o caminho definido na vari??vel PATH.
    Se o formato do relat??rio escolhido pelo usu??rio for "pdf", o m??todo JasperExportManager.exportReportToPdfFile ?? chamado para exportar o relat??rio em formato PDF para o caminho definido na vari??vel PATH.
    Por fim, o m??todo retorna uma string indicando o caminho onde o relat??rio foi gerado.
* */
