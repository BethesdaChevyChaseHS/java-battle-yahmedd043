package bcc.javaJostle;

import java.util.ArrayList;

public class MyRobot extends Robot{
    public MyRobot(int x, int y){
        super(x, y, 4, 1, 2, 3,"Robob", "myRobotImage.png", "defaultProjectileImage.png");
        // Health: 4, Speed: 2, Attack Speed: 3, Projectile Strength: 1
        // Total = 10
        // Image name is "myRobotImage.png"
    }

    public void think(ArrayList<Robot> robots, ArrayList<Projectile> projectiles, Map map, ArrayList<PowerUp> powerups) {
       /* Implement your robot's logic here
        For example, you can move towards the nearest robot or shoot at a target
        to move, choose a direciton to go
        to move left - use xMovement = -1
        to move right - use xMovement = 1
        to move up - use yMovement = -1
        to move down - use yMovement = 1
        You can ONLY move in one direction at a time, if your output doesn't match the above you will get an error

        to shoot, use shootAtLocation(x, y) where x and y are the coordinates of the target
        only shoot when canAttack() is true!        
        */
        System.out.println("Thinking...");
        Robot targetRobot = targetRobot(robots);
        if (targetRobot != null) {
            move(targetRobot, map);
            if(canAttack())
            {
                // shootAtLocation(targetRobot.getX() + Utilities.ROBOT_SIZE / 2, targetRobot.getY() + Utilities.ROBOT_SIZE / 2);
                sprayShoot(targetRobot);
            }
        }
    }

    // return the nearest robot
    private Robot targetRobot(ArrayList<Robot> robots) {
        Robot targetRobot = null;
        double minDist = Double.MAX_VALUE;
        for (Robot robot : robots) {
            double dist = Math.hypot((getX() + Utilities.ROBOT_SIZE / 2) - (robot.getX() + Utilities.ROBOT_SIZE / 2), (getY() + Utilities.ROBOT_SIZE / 2) - (robot.getY() + Utilities.ROBOT_SIZE / 2));
            if (dist < minDist && robot != this) {
                minDist = dist; 
                targetRobot = robot;
            }
        }
        return targetRobot;
    }

    // movement method, kept here to keep the code organized
    private void move(Robot targetRobot, Map map) {
        xMovement = 0;
        yMovement = 0;

        double x1 = getX() + Utilities.ROBOT_SIZE / 2;
        double y1 = getY() + Utilities.ROBOT_SIZE / 2;
        double x2 = targetRobot.getX() + Utilities.ROBOT_SIZE / 2;
        double y2 = targetRobot.getY() + Utilities.ROBOT_SIZE / 2;

        double dx = Math.abs(x2 - x1);
        double dy = Math.abs(y2 - y1);

        int tileLeft = getOccupiedTiles()[0];
        int tileRight = getOccupiedTiles()[1];
        int tileTop = getOccupiedTiles()[2];
        int tileBottom = getOccupiedTiles()[3];

        int[][] tiles = map.getTiles();
        if (dx >= dy) {
            if (x1 < x2) { // move right
                if (tiles[tileTop][tileRight + 1] != Utilities.WALL && tiles[tileBottom][tileRight + 1] != Utilities.WALL &&
                    tiles[tileTop][tileRight + 1] != Utilities.MUD && tiles[tileBottom][tileRight + 1] != Utilities.MUD) {
                    xMovement = 1;
                } else if (tiles[tileTop - 1][tileLeft] != Utilities.WALL && tiles[tileLeft][tileTop - 1] != Utilities.MUD) {
                    yMovement = -1;
                } else if (tiles[tileBottom + 1][tileLeft] != Utilities.WALL && tiles[tileLeft][tileBottom + 1] != Utilities.MUD) {
                    yMovement = 1;
                }
            } else { // move left
                if (tiles[tileTop][tileLeft - 1] != Utilities.WALL && tiles[tileBottom][tileLeft - 1] != Utilities.WALL &&
                    tiles[tileTop][tileLeft - 1] != Utilities.MUD && tiles[tileBottom][tileLeft - 1] != Utilities.MUD) {
                    xMovement = -1;
                } else if (tiles[tileTop - 1][tileLeft] != Utilities.WALL && tiles[tileLeft][tileTop - 1] != Utilities.MUD) {
                    yMovement = -1;
                } else if (tiles[tileBottom + 1][tileLeft] != Utilities.WALL && tiles[tileLeft][tileBottom + 1] != Utilities.MUD) {
                    yMovement = 1;
                }
            }
        } else {
            if (y1 < y2) { // move down
                if (tiles[tileBottom + 1][tileLeft] != Utilities.WALL && tiles[tileBottom + 1][tileRight] != Utilities.WALL &&
                    tiles[tileBottom + 1][tileLeft] != Utilities.MUD && tiles[tileBottom + 1][tileRight] != Utilities.MUD) {
                    yMovement = 1;
                } else if (tiles[tileTop][tileRight + 1] != Utilities.WALL && tiles[tileTop][tileRight + 1] != Utilities.MUD) {
                    xMovement = 1;
                } else if (tiles[tileTop][tileLeft - 1] != Utilities.WALL && tiles[tileTop][tileLeft - 1] != Utilities.MUD) {
                    xMovement = -1;
                }
            } else { // move up
                if (tiles[tileTop - 1][tileLeft] != Utilities.WALL && tiles[tileTop - 1][tileRight] != Utilities.WALL &&
                    tiles[tileTop - 1][tileLeft] != Utilities.MUD && tiles[tileTop - 1][tileRight] != Utilities.MUD) {
                    yMovement = -1;
                } else if (tiles[tileTop][tileRight + 1] != Utilities.WALL && tiles[tileTop][tileRight + 1] != Utilities.MUD) {
                    xMovement = 1;
                } else if (tiles[tileTop][tileLeft - 1] != Utilities.WALL && tiles[tileTop][tileLeft - 1] != Utilities.MUD) {
                    xMovement = -1;
                }
            }
        }
    }

    private int[] getOccupiedTiles() {
        int left = getX();
        int right = getX() + Utilities.ROBOT_SIZE - 1;
        int top = getY();
        int bottom = getY() + Utilities.ROBOT_SIZE - 1;

        int tileLeft = left / Utilities.TILE_SIZE;
        int tileRight = right / Utilities.TILE_SIZE;
        int tileTop = top / Utilities.TILE_SIZE;
        int tileBottom = bottom / Utilities.TILE_SIZE;

        // returns [left, right, top, bottom] tile indices
        return new int[] { tileLeft, tileRight, tileTop, tileBottom };
    }

    // attack with a spray style
    private void sprayShoot(Robot targetRobot) {
        int distanceX = (getX() + Utilities.ROBOT_SIZE / 2) - (targetRobot.getX() + Utilities.ROBOT_SIZE / 2);
        int distanceY = (getY() + Utilities.ROBOT_SIZE / 2) - (targetRobot.getY() + Utilities.ROBOT_SIZE / 2);
        double angle = Math.atan2(distanceY, distanceX);
        double offset = (Math.random() * 15) - 7.5;
        double distance = Math.hypot(getX() - targetRobot.getX(), getY() - targetRobot.getY());
        shootAtLocation(getX() + Utilities.ROBOT_SIZE / 2 - (int) (Math.cos(angle + (Math.PI /180 * offset)) * distance), getY() + Utilities.ROBOT_SIZE / 2 - (int) (Math.sin(angle + (Math.PI / 180 * offset)) * distance)); 
    }
}
