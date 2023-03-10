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
            // Define que o arquivo pode ser visualizado no navegador e também nome final do arquivo
            // para fazer download do relatório troque 'inline' por 'attachment'
            response.setHeader("Content-Disposition", "attachment; filename=relatorio.html");
             JasperExportManager.exportReportToHtmlFile(jasperPrint, PATH + "\\clientes.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            response.setContentType("application/pdf");
            // Define que o arquivo pode ser visualizado no navegador e também nome final do arquivo
            // para fazer download do relatório troque 'inline' por 'attachment'
            response.setHeader("Content-Disposition", "attachment; filename=livros.pdf");
            JasperExportManager.exportReportToPdfFile(jasperPrint, PATH + "\\relatorio.pdf");
        }

        final OutputStream outStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
    }
}


/*
*
* O método exportReport tem como objetivo gerar relatórios para uma lista de clientes, no formato escolhido pelo usuário (PDF ou HTML). Ele recebe como parâmetro uma string reportFormat, que indica qual formato deve ser utilizado.
    O primeiro passo do método é definir o caminho onde o arquivo será salvo, através da variável PATH. Em seguida, é feita a consulta dos clientes no repositório, através do método findAll(), e os resultados são armazenados na lista clientes.
    O próximo passo é carregar o arquivo clientes.jrxml, que contém o modelo do relatório, utilizando a classe ResourceUtils e compilá-lo através da classe JasperCompileManager. O resultado é armazenado na variável jasperReport.
    Os dados dos clientes são passados para o relatório através de um objeto JRBeanCollectionDataSource, que é criado a partir da lista clientes.
    Um mapa de parâmetros é criado para ser utilizado pelo relatório, que neste caso contém apenas a chave "createdBy" e o valor "Java Techie".
    O relatório é preenchido com os dados dos clientes utilizando a classe JasperFillManager e o resultado é armazenado na variável jasperPrint.
    Se o formato do relatório escolhido pelo usuário for "html", o método JasperExportManager.exportReportToHtmlFile é chamado para exportar o relatório em formato HTML para o caminho definido na variável PATH.
    Se o formato do relatório escolhido pelo usuário for "pdf", o método JasperExportManager.exportReportToPdfFile é chamado para exportar o relatório em formato PDF para o caminho definido na variável PATH.
    Por fim, o método retorna uma string indicando o caminho onde o relatório foi gerado.
* */
