package br.com.fiap.orangeroute_oracle.oracledao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class RelatorioJsonDAO {

    @Autowired
    private DataSource dataSource;

    public String executarRelatorioJson() {

        String sql = "{ call prc_relatorio_json(?) }";

        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, Types.CLOB);

            stmt.execute();

            Clob clob = stmt.getClob(1);
            String resultado = (clob != null)
                    ? clob.getSubString(1, (int) clob.length())
                    : null;

            System.out.println("\n==============================================");
            System.out.println(" RELATÓRIO JSON");
            System.out.println("==============================================");
            System.out.println(resultado);

            return resultado;

        } catch (SQLException e) {

            String erro = e.getMessage();

            if (erro.contains("-20060")) {
                System.err.println("Regra de negócio: Nenhum comentário encontrado.");
            } else {
                System.err.println("Erro ao executar relatório JSON: " + erro);
            }

            return null;
        }
    }
}