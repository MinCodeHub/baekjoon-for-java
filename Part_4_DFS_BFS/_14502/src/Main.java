import java.io.*;
import java.util.*;

public class Main {

    //14502 -  연구소 미해결
    static final int dx[] = {0,0,1,-1};  //상하좌우 방향 설정
    static final int dy[] = {1,-1,0,0};  //상화좌우 방향 설정
    static int originalMap[][];
    static int n,m;
    static int maxSafeZone = Integer.MIN_VALUE; //최대값을 찾기 위한 최소값 설정

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        originalMap = new int[n][m];


        //원본 맵
        for(int i=0; i<n; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<m; j++) {
                originalMap[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        //dfs호출
        dfs(0);

        System.out.println(maxSafeZone);
    }

    //전략 자체가 그냥 벽을 설치할 수 있는 곳엔 모두 설치해보고 최대 세이프 존을 찾아보자

    static void dfs(int wallCnt) {
        //벽이 3개가 설치 된 경우 bfs 탐색 시작
        if(wallCnt == 3) {
            bfs();
            return;
        }

        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                if(originalMap[i][j] == 0) {
                    originalMap[i][j] = 1;
                    dfs(wallCnt+1);
                    originalMap[i][j] = 0; //백트레킹 -> 치킨 배달 문제 먼저 풀어보라는 영상이 있음
                }
            }
        }
    }

    static void bfs() {
        Queue<Node> q = new LinkedList<>();

        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                if(originalMap[i][j] == 2) {
                    q.add(new Node(i,j));
                }
            }
        }

        //원본 연구소를 바꾸지 않기 위한 카피맵 사용
        int copyMap[][] = new int[n][m];

        for (int i = 0; i < n; i++) {
            copyMap[i] = originalMap[i].clone();
        }

        //BFS 탐색 시작
        while(!q.isEmpty()) {
            Node now = q.poll();
            int x = now.x; // 현재 값
            int y = now.y; //

            for(int k=0; k<4; k++) {
                int nx = x + dx[k];
                int ny = y + dy[k];

                //연구소 범위 밖이 아니고 빈칸일 경우에만 바이러스를 퍼트린다.
                if(0<=nx && nx<n && 0<=ny && ny<m) {
                    if(copyMap[nx][ny] == 0) {
                        q.add(new Node(nx,ny));
                        copyMap[nx][ny] = 2;
                    }
                }
            }
        }

        //SafeZone 확인
        funcSafeZone(copyMap);
    }

    private static void funcSafeZone(int[][] copyMap) {
        int safeZone =0;
        for(int i=0; i<n ; i++) {
            for(int j=0; j<m; j++) {
                if(copyMap[i][j] == 0) {
                    safeZone++;
                }
            }
        }
        if (maxSafeZone < safeZone) {
            maxSafeZone = safeZone;
        }
    }

    //Queue에 좌표값 x,y를 넣기 위함.
    static class Node {
        int x;
        int y;
        Node(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
}