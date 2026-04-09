package br.com.fiap.orangeroute_oracle.oracledao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class ComentarioJsonDAO {

    @Autowired
    private DataSource dataSource;

    public String listarComentariosJson() {

        String sql = "{ ? = call fn_lista_json_comentarios }";

        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, java.sql.Types.CLOB);

            stmt.execute();

            String json = stmt.getString(1);

            System.out.println("JSON de todos os comentários:");
            System.out.println(json);

            return json;

        } catch (SQLException e) {

            System.err.println("Erro ao gerar JSON de comentários: " + e.getMessage());
            return null;
        }
    }
}