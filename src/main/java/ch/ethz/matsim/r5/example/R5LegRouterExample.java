package ch.ethz.matsim.r5.example;

import java.io.File;
import java.util.List;

import org.matsim.core.utils.geometry.transformations.WGS84toCH1903LV03Plus;
import org.matsim.core.utils.misc.Time;

import com.conveyal.r5.transit.TransportNetwork;

import ch.ethz.matsim.r5.R5LegRouter;
import ch.ethz.matsim.r5.distance.CrowflyDistanceEstimator;
import ch.ethz.matsim.r5.distance.DistanceEstimator;
import ch.ethz.matsim.r5.route.R5Leg;
import ch.ethz.matsim.r5.scoring.R5ItineraryScorer;
import ch.ethz.matsim.r5.scoring.SoonestArrivalTimeScorer;
import ch.ethz.matsim.r5.utils.R5Cleaner;
import ch.ethz.matsim.r5.utils.spatial.DefaultLatLonToCoord;
import ch.ethz.matsim.r5.utils.spatial.LatLon;
import ch.ethz.matsim.r5.utils.spatial.LatLonToCoordTransformation;

public class R5LegRouterExample {
	static public void main(String[] args) throws Exception {
		String path = "/home/sebastian/r5/input/network.dat";
		TransportNetwork transportNetwork = TransportNetwork.read(new File(path));
		new R5Cleaner(transportNetwork).run();

		String day = "2017-09-25";
		String timezone = "+02:00";

		LatLonToCoordTransformation latLonToCoord = new DefaultLatLonToCoord(new WGS84toCH1903LV03Plus());
		R5ItineraryScorer scorer = new SoonestArrivalTimeScorer();
		DistanceEstimator distanceEstimator = new CrowflyDistanceEstimator(latLonToCoord, 1.0);
		R5LegRouter router = new R5LegRouter(transportNetwork, scorer, distanceEstimator, day, timezone);

		LatLon fromLocation = new LatLon(46.878275381782174, 8.21909674274457);
		LatLon toLocation = new LatLon(46.86166306929253, 8.233059319342907);
		double departureTime = 62605.0;

		List<R5Leg> route = router.route(fromLocation, toLocation, departureTime, null);

		for (R5Leg leg : route) {
			System.out.println(String.format("Mode: %s, Start: %s, Duration: %s", leg.getClass().toString(),
					Time.writeTime(leg.getDepartureTime()), Time.writeTime(leg.getTravelTime())));
		}
	}
}
