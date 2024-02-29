
import java.util.LinkedList;

public class BaseballElimination {

  private class Team implements Comparable<Team> {
      private LinkedList<Integer> who;
      final private String name;
      final private int wins;
      final private int losses;
      final private int remaining;
      final private int index;
      final private int[] g;

      Team(String name,
           int wins,
           int losses,
           int remaining,
           int[] g,
           int index) {
          this.name = name;
          this.wins = wins;
          this.losses = losses;
          this.remaining = remaining;
          this.g = g;
          this.index = index;
          this.who = null;              // who elimimates this Team ?
      }

      public int compareTo(Team that) {
          return name.compareTo(that.name);
      }

      public String toString() {
          StringBuilder s = new StringBuilder();
          for (int i : g)
              s.append(String.format(" %6d", i));
          if (who != null) {
              s.append("  [");
              for (int i : who)
                  s.append(String.format(" %s ", teams[i].name));
              s.append("]");
          }
          else
              s.append("  UNRESOLVED");
          return (String.format("%d -- %15.16s %4d %4d %4d   -|-",
                                index,
                                name,
                                wins,
                                losses,
                                remaining)
                  + s.toString());
      }

      // TeamX index. In case that x is greater than this.index, we do
      // not want to use the INDEX for teamX, because this is ommited.
      public Integer ind(Integer x) {
          if (x == index) return null;
          return (x > index) ? x-1 : x;
      }

      // given an index, returns the TEAM vertex
      public int indexToVertex (int x) {
          return 1 + numberOfGames + ind(x);
      }

      public int vertexToIndex (int v) {
          int p = v - (numberOfGames + 1);
          if (p >= index) p++;
          return teams[p].index;
      }

  }


  final private int numberOfTeams;
  final private int numberOfGames;
  final private int numberOfVertices;
  final private int numberOfEdges;
  final private Team[] teams;
  final private ST<String, Team> teamsST;

  // create a baseball division from given filename in format
  // specified below
  public BaseballElimination(String filename) {
      In in = new In(filename);
      numberOfTeams = in.readInt();
      teams = new Team[numberOfTeams];
      teamsST = new ST<String, Team>();
      int i = 0;
      while(!in.isEmpty()) {
          String name = in.readString();
          int wins = in.readInt();
          int losses = in.readInt();
          int remaining = in.readInt();
          int [] g = new int[numberOfTeams];
          for (int j = 0; j < numberOfTeams; j++)
              g[j] = in.readInt();
          Team team = new Team(name, wins, losses, remaining, g, i);
          teams[i] = team;
          teamsST.put(name, team);
          i++;
      }
      in.close();
      numberOfGames = (numberOfTeams*(numberOfTeams-1))/2 -(numberOfTeams-1);
      numberOfVertices = 2 + numberOfTeams - 1 + numberOfGames;
      numberOfEdges = (numberOfGames      // from START
                       +2*numberOfGames   // from GAMES to TEAMS
                       +numberOfTeams-1); // from TEAMS to END
      // System.out.println(this.toString2());
      // System.out.println();
  }

  private void solve(Team team) {

      if (team.who != null) return;      // already solved

      team.who = new LinkedList<Integer>(); // init solution

      Integer teamI, teamJ;
      int gamePos = 1, gameI, gameJ;
      FlowNetwork G = new FlowNetwork(numberOfVertices);

      // add edges from START to each GAME node(GAMEI, GAMEJ) and
      // edges from each game node(GAMEI, GAMEJ) to TEAM nodes TEAMI
      // and TEAMJ.
      for (gameI = 0; gameI < numberOfTeams; gameI++) {
          teamI = team.ind(gameI);
          if (teamI == null)
              continue;
          for (gameJ = gameI+1; gameJ < numberOfTeams; gameJ++) {
              teamJ = team.ind(gameJ);
              if (teamJ == null)
                  continue;
              FlowEdge Z = new FlowEdge(0, gamePos, against(teams[gameI].name,
                                                            teams[gameJ].name));
              G.addEdge(Z);
              G.addEdge(new FlowEdge(gamePos, team.indexToVertex (gameI),
                                     Integer.MAX_VALUE));
              G.addEdge(new FlowEdge(gamePos, team.indexToVertex (gameJ),
                                     Integer.MAX_VALUE));
              gamePos++;
          }
          int weight = team.wins + team.remaining - teams[gameI].wins;
          if (weight < 0) {
              // found a simple elimination. no reason to continue;
              team.who.add(gameI);
              return;
          }
          G.addEdge(new FlowEdge(team.indexToVertex (gameI),
                                 numberOfVertices-1, weight));
      }

      // The edge from TeamI to END has a capacity of maximum number
      // of goals that TEAMI can mark in order NOT to defend teamX. In
      // case that the capacity from START of any GAME edge is not
      // filled after the maxflow had been detected, that means that
      // the maximum number of wins to EQUAL teamX was accomplished,
      // and the remaining capacity-flow are the goals that will
      // defend teamX. The mincut will run through these unfilled
      // edges. The TEAM nodes reached by these unfilled edges are the
      // teams that will defend the teamX, because they still have
      // matches to play after having EQUALED teamX...

      FordFulkerson maxflow = new FordFulkerson(G, 0, G.V()-1);
      // System.out.printf("\n%s\n", G);
      for (int v = 0; v < G.V(); v++)
          if (maxflow.inCut(v))
              if (v > numberOfGames)
                  team.who.addLast(team.vertexToIndex (v));
  }


  // the grader does not accept toString() as a public method
  private String toString2() {
      StringBuilder s = new StringBuilder();
      for (Team t : teams)
          s.append(String.format(" %s\n", t));
      return s.toString();
  }

  // number of teams
  public int numberOfTeams() {
      return numberOfTeams;
  }

  // all teams
  public Iterable<String> teams() {
      LinkedList<String> res = new LinkedList<String>();
      for (Team t : teams)
          res.add(t.name);
      return res;
  }

  // number of wins for given team
  public int wins(String team) {
      if (teamsST.get(team) == null)
          throw new java.lang.IllegalArgumentException();
      return teamsST.get(team).wins;
  }

  // number of losses for given team
  public int losses(String team) {
      if (teamsST.get(team) == null)
          throw new java.lang.IllegalArgumentException();
      return teamsST.get(team).losses;
  }

  // number of remaining games for given team
  public int remaining(String team) {
      if (teamsST.get(team) == null)
          throw new java.lang.IllegalArgumentException();
      return teamsST.get(team).remaining;
  }

  // number of remaining games between team1 and team2
  public int against(String team1, String team2) {
      if (teamsST.get(team1) == null)
          throw new java.lang.IllegalArgumentException();
      if (teamsST.get(team2) == null)
          throw new java.lang.IllegalArgumentException();
      Team t1 = teamsST.get(team1);
      Team t2 = teamsST.get(team2);
      return t1.g[t2.index];
  }

  // is given team eliminated?
  public boolean isEliminated(String team) {
      if (teamsST.get(team) == null)
          throw new java.lang.IllegalArgumentException();
      Team team0 = teamsST.get(team);
      solve(team0);
      // System.out.printf("---\n%s---\n", this.toString2());
      return team0.who.size() != 0;
  }

  // subset R of teams that eliminates given team; null if not
  // eliminated
  public Iterable<String> certificateOfElimination(String team) {
      if (teamsST.get(team) == null)
          throw new java.lang.IllegalArgumentException();
      LinkedList<String> R = new LinkedList<String>();
      Team team0 = teamsST.get(team);
      solve(team0);
      for (int i : team0.who)
          R.add(teams[i].name);
      return R.size() > 0 ? R:null;
  }

  public static void main(String[] args) {
      BaseballElimination division = new BaseballElimination(args[0]);
      for (String team : division.teams()) {
          if (division.isEliminated(team)) {
              StdOut.print(team + " is eliminated by the subset R = { ");
              for (String t : division.certificateOfElimination(team))
                  StdOut.print(t + " ");
              StdOut.println("}");
          }
          else {
              StdOut.println(team + " is not eliminated");
          }
      }
  }
}


