package br.com.fiap.orangeroute_oracle.oracledao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class ComentarioDAO {

    @Autowired
    private DataSource dataSource;

    public void inserirComentario(String conteudo, String ativo, int idUsuario, int idTrilhaCarreira) {
        String procedure = "{call prc_insere_comentario(?, ?, ?, ?)}";

        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(procedure)) {

            stmt.setString(1, conteudo);
            stmt.setString(2, ativo);
            stmt.setInt(3, idUsuario);
            stmt.setInt(4, idTrilhaCarreira);

            stmt.execute();

            System.out.println("INSERT executado (Comentário): Usuário " + idUsuario + " → Trilha " + idTrilhaCarreira);

        } catch (SQLException e) {

            String erro = e.getMessage();

            if (erro.contains("-20900")) {
                System.err.println("Regra de negócio: Trilha com muitas interações. Comentário bloqueado.");
            } else if (erro.contains("-20200")) {
                System.err.println("Erro: Comentário não pode ser vazio.");
            } else if (erro.contains("-20201")) {
                System.err.println("Erro: Status inválido (use 0 ou 1).");
            } else {
                System.err.println("Erro geral ao inserir comentário: " + erro);
            }
        }
    }

    public void atualizarComentario(int idComentario, String conteudo, String ativo, int idUsuario, int idTrilhaCarreira) {
        String procedure = "{call prc_update_comentario(?, ?, ?, ?, ?)}";

        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(procedure)) {

            stmt.setInt(1, idComentario);
            stmt.setString(2, conteudo);
            stmt.setString(3, ativo);
            stmt.setInt(4, idUsuario);
            stmt.setInt(5, idTrilhaCarreira);

            stmt.execute();

            System.out.println("UPDATE executado (Comentário ID: " + idComentario + ")");

        } catch (SQLException e) {
            System.err.println("Erro UPDATE (Comentário): " + e.getMessage());
        }
    }

    public void deletarComentario(int idComentario) {
        String procedure = "{call prc_delete_comentario(?)}";

        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(procedure)) {

            stmt.setInt(1, idComentario);
            stmt.execute();

            System.out.println("DELETE executado (Comentário ID: " + idComentario + ")");

        } catch (SQLException e) {
            System.err.println("Erro DELETE (Comentário): " + e.getMessage());
        }
    }
}