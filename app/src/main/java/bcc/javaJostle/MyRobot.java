package bcc.javaJostle;

import java.util.ArrayList;

public class MyRobot extends Robot{
    public MyRobot(int x, int y){
        super(x, y, 5, 1, 1, 3,"Robob", "myRobotImage.png", "defaultProjectileImage.png");
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
                shootAtRobot(targetRobot);
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

        int tileX = (int) (x1 / Utilities.TILE_SIZE);
        int tileY = (int) (y1 / Utilities.TILE_SIZE);

        int[][] tiles = map.getTiles();

        if (dx >= dy) {
            if (x1 < x2) {
                if (tiles[tileY][tileX + 1] != Utilities.WALL) {
                    xMovement = 1;
                } else if (tiles[tileY - 1][tileX] != Utilities.WALL) {
                    yMovement = -1;
                } else {
                    yMovement = 1;
                }
            } else {
                if (tiles[tileY][tileX - 1] != Utilities.WALL) {
                    xMovement = -1;
                } else if (tiles[tileY - 1][tileX] != Utilities.WALL) {
                    yMovement = -1;
                } else {
                    yMovement = 1;
                }
            }
        } else {
            if (y1 < y2) {
                if (tiles[tileY + 1][tileX] != Utilities.WALL) {
                    yMovement = 1;
                } else if (tiles[tileY][tileX + 1] != Utilities.WALL) {
                    xMovement = 1;
                } else {
                    xMovement = -1;
                }
            } else {
                if (tiles[tileY - 1][tileX] != Utilities.WALL) {
                    yMovement = -1;
                } else if (tiles[tileY][tileX + 1] != Utilities.WALL) {
                    xMovement = 1;
                } else {
                    xMovement = -1;
                }
            }
        }
    }

    // attack with a spray style
    private void shootAtRobot(Robot targetRobot) {
        int distanceX = getX() - targetRobot.getX();
        int distanceY = getY() - targetRobot.getY();
        double angle = Math.atan2(distanceY, distanceX);
        double offset = (Math.random() * 15) - 7.5;
        double distance = Math.hypot(getX() - targetRobot.getX(), getY() - targetRobot.getY());
        shootAtLocation(getX() + Utilities.ROBOT_SIZE / 2 - (int) (Math.cos(angle + (Math.PI /180 * offset)) * distance), getY() + Utilities.ROBOT_SIZE / 2 - (int) (Math.sin(angle + (Math.PI / 180 * offset)) * distance)); 
    }
}
