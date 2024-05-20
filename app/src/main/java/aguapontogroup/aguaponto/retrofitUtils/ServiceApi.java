package aguapontogroup.aguaponto.retrofitUtils;

import aguapontogroup.aguaponto.model.BodyGetllAllUsers;
import aguapontogroup.aguaponto.model.BodyPostRotina;
import aguapontogroup.aguaponto.model.UsuarioModel;
import aguapontogroup.aguaponto.model.BodyGetRotinas;
import aguapontogroup.aguaponto.model.UsuarioModelToBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceApi {

    @GET("api/usuarios/GetAllUsuarios")
    Call<BodyGetllAllUsers> getAllUsers();

    @POST("api/rotina/PostRotina")
    Call<Void> postRotina(@Body BodyPostRotina rotinaModel);

    @DELETE("api/rotina/DeleteRotina/{id}")
    Call<Void> deletarRotina(@Path("id") int id);

    @GET("api/rotina/GetRotinaByUsuarioId/{id}")
    Call<BodyGetRotinas> getRotinas(@Path("id") int id);

    @GET("api/usuarios/GetUsuario/{id}")
    Call<UsuarioModel> getUser(@Path("id") int id);

    @POST("api/usuarios/PostUsuario")
    Call<UsuarioModel> postUsuario(@Body UsuarioModelToBody usuarioModel);

    @PUT("/api/usuarios/UpdateUsuario/{id}")
    Call<Void> updateUsuario(@Body UsuarioModelToBody usuarioModel, @Path("id") int id);

}
