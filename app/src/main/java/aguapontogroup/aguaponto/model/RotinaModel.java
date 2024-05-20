package aguapontogroup.aguaponto.model;

public class RotinaModel {

    int id;
    int mlIngerido, usuarioId;
    String ingestao;

    public RotinaModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIngestao() {
        return ingestao;
    }

    public void setIngestao(String ingestao) {
        this.ingestao = ingestao;
    }

    public int getMlIngerido() {
        return mlIngerido;
    }

    public void setMlIngerido(int mlIngerido) {
        this.mlIngerido = mlIngerido;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
}
