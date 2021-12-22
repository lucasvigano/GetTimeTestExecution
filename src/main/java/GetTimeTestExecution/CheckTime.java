package GetTimeTestExecution;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import vrframework.classe.Format;
import vrframework.classe.Texto;
import vrframework.remote.Arquivo;

public class CheckTime {

    public void iniciar() throws Exception {
        List<ResultTestCase> vTesteCaseResult = getResultTestCases();

        List<String> vLinhas = new ArrayList();

        vLinhas.addAll(processaRetorno(vTesteCaseResult));

        vLinhas.addAll(getErrors(vTesteCaseResult));

        gerarArquivo(vLinhas);
    }

    private List<File> getFiles() throws Exception {
//        String i_path = "\\build\\test-results\\test\\";
        String i_path = "C:\\git\\4.1\\VRConcentrador\\build\\test-results\\test";

        if (!Arquivo.exists(i_path)) {
            throw new Exception("Path vazio ou nao encontrado. " + i_path);
        }

        File diretorio = new File(i_path);
        File arquivos[] = diretorio.listFiles();

        if (arquivos == null || arquivos.length == 0) {
            return null;
        }

        List<File> vFile = new ArrayList();

        for (File arquivo : arquivos) {
            if (arquivo.isDirectory()) {
                continue;
            }

            if (!arquivo.getName().contains(".xml")) {
                continue;
            }

            vFile.add(arquivo);
        }

        return vFile;
    }

    public List<ResultTestCase> getResultTestCases() throws Exception {
        List<ResultTestCase> vRetorno = new ArrayList<>();
        List<File> vArquivos = getFiles();

        for (File pArquivo : vArquivos) {
            String xml = FileUtils.readFileToString(pArquivo, "utf-8");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            docBuilder.setErrorHandler(null);
            Document doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("utf-8")));

            Element testsuite = (Element) doc.getChildNodes().item(0);
            NodeList vTest = testsuite.getElementsByTagName("testcase");
            Element error = (Element) testsuite.getElementsByTagName("system-err").item(0);

            ResultTestCase result = new ResultTestCase();

            String classe = testsuite.getAttribute("name");

            for (int i = 0; i < vTest.getLength(); i++) {
                Element testCase = (Element) vTest.item(i);

                result.getvTestCase().add(new TestCase(new Double(testCase.getAttribute("time")), testCase.getAttribute("classname"), testCase.getAttribute("name")));
            }

            result.setTotalTime(new Double(testsuite.getAttribute("time")));
            result.setClasse(classe);
            result.setError(error.getTextContent());

            vRetorno.add(result);
        }

        return vRetorno;
    }

    private List<String> getErrors(List<ResultTestCase> vResult) throws Exception {
        List<String> vRetorno = new ArrayList<>();

        vRetorno.add("");
        vRetorno.add("");
        vRetorno.add(Texto.repeatString("#", 50) + " ERROS " + Texto.repeatString("#", 50));
        vRetorno.add("");
        vRetorno.add("");

        vResult.stream().forEach(oResult -> {
            if (!oResult.getError().isEmpty()) {
                vRetorno.add("Classe: " + oResult.getClasse() + "\nErro: " + oResult.getError());
            }
        });

        return vRetorno;
    }

    private List<String> processaRetorno(List<ResultTestCase> vResult) throws Exception {
        List<TestCase> vAux = new ArrayList<>();

        vResult.stream().forEach(oResult -> vAux.addAll(oResult.getvTestCase()));

        Collections.sort(vAux, new TestCaseComparator());

        List<String> vRetorno = new ArrayList<>();
        double tempoTotal = 0;

        for (TestCase oResult : vAux) {
            vRetorno.add("Tempo Execução: " + Format.decimal3(oResult.getExecutionTime()) + "s - Classe: " + Format.string(oResult.getClasse(), 80) + " Método: " + oResult.getTest());
            tempoTotal += oResult.getExecutionTime();
        }

        vRetorno.add("===================== TEMPO EXECUCAO TOTAL = " + Format.decimal3(tempoTotal) + "s");

        return vRetorno;
    }

    public class TestCaseComparator implements Comparator<TestCase> {

        @Override
        public int compare(TestCase a1, TestCase a2) {
            return (a1.getExecutionTime() > a2.getExecutionTime()) ? -1 : 1;
        }
    }

    private boolean gerarArquivo(List<String> vRetorno) {
        try {
            Arquivo a = new Arquivo(".\\LogTeste.txt", "w");
            a.write(vRetorno);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
