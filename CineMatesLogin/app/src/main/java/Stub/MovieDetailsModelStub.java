package Stub;

import com.example.cinemates.ui.CineMates.ApiMovie.ApiModel.MovieDetailsModel;
import com.example.cinemates.ui.CineMates.ApiMovie.model.Crew;

import java.util.ArrayList;

public class MovieDetailsModelStub extends MovieDetailsModel {

    public String getMovieDetails(ArrayList<String> nomi, ArrayList<String> jobs, int dim) {
        if (nomi != null || jobs != null || dim >= 0) {
            if (nomi.size() != jobs.size() || nomi.size() != dim)
                return null;
            String regista = "";
            ArrayList<Crew> crews = new ArrayList<>();
            for (int i = 0; i < dim; i++) {
                Crew crew = new Crew(nomi.get(i), jobs.get(i));
                crews.add(crew);
            }

            for (Crew crew : crews) {
                if (crew.getJob().equals("Director"))
                    if (regista.equals(""))
                        regista = crew.getName();
                    else
                        regista = regista + ", " + crew.getName();
            }
            return regista;
        } else
            return null;
    }

}
