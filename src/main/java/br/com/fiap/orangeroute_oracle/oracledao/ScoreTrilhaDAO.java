package br.com.fiap.orangeroute_oracle.oracledao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class ScoreTrilhaDAO {

    @Autowired
    private DataSource dataSource;

    public int calcularScore(int idTrilha) {

        String sql = "{ ? = call fn_score_trilha(?) }";

        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, java.sql.Types.INTEGER);
            stmt.setInt(2, idTrilha);

            stmt.execute();

            int score = stmt.getInt(1);

            System.out.println("Score da trilha ID " + idTrilha + ": " + score);

            return score;

        } catch (SQLException e) {

            System.err.println("Erro ao calcular score da trilha: " + e.getMessage());
            return -1;
        }
    }
}