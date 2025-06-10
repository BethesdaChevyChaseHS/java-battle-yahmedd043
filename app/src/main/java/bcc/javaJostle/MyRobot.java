package bcc.javaJostle;

import java.util.ArrayList;

public class MyRobot extends Robot{
    private static final int STEP_SIZE = 2;
    private boolean isInSight;
    private boolean hasMoved;

    public MyRobot(int x, int y){
        super(x, y, 3, 1, 3, 3,"Robob", "myRobotImage.png", "defaultProjectileImage.png");
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
        Robot targetRobot = nearestRobot(robots);
        if (targetRobot != null) {
            isInSight = checkInSight(targetRobot, map);
            if (!isInSight) {
                // if the robot isn't in sight, move towards it
                move(targetRobot, map);

                // re-evaluate if the robot is in sight
                isInSight = checkInSight(targetRobot, map);
            }
            if (isInSight && canAttack()) {
                // if the robot is in sight and can attack, shoot at it
                shootAtLocation(targetRobot.getX(), targetRobot.getY());
            }
        }    
    }

    //
    // helper methods!!
    // EXTREMELY IMPORTANT
    //

    
    // return the nearest robot
    private Robot nearestRobot(ArrayList<Robot> robots) {
        Robot nearestRobot = null;
        double minDist = Double.MAX_VALUE;
        for (Robot robot : robots) {
            double dist = Math.hypot((getX() + Utilities.ROBOT_SIZE / 2) - (robot.getX() + Utilities.ROBOT_SIZE / 2), (getY() + Utilities.ROBOT_SIZE / 2) - (robot.getY() + Utilities.ROBOT_SIZE / 2));
            if (dist < minDist && robot != this) {
                minDist = dist; 
                nearestRobot = robot;
            }
        }
        return nearestRobot;
    }

    // movement method, kept here to keep the code organized
    private void move(Robot targetRobot, Map map) {
        xMovement = 0;
        yMovement = 0;

        double x1 = getX() + Utilities.ROBOT_SIZE / 2;
        double y1 = getY() + Utilities.ROBOT_SIZE / 2;
        double x2 = targetRobot.getX() + Utilities.ROBOT_SIZE / 2;
        double y2 = targetRobot.getY() + Utilities.ROBOT_SIZE / 2;

        if (x1 < x2) {
            xMovement = 1;
        } else if (x1 > x2) {
            xMovement = -1;
        } else if (y1 < y2) {
            yMovement = 1;
        } else if (y1 > y2) {
            yMovement = -1;
        }
    }

    // check if the target robot is in sight
    // being in sight is defined as there being no obstacles between the robot and the target robot
    private boolean checkInSight(Robot targetRobot, Map map) {
        double x1 = getX() + Utilities.ROBOT_SIZE / 2;
        double y1 = getY() + Utilities.ROBOT_SIZE / 2;
        double x2 = targetRobot.getX() + Utilities.ROBOT_SIZE / 2;
        double y2 = targetRobot.getY() + Utilities.ROBOT_SIZE / 2;

        double dx = x2 - x1;
        double dy = y2 - y1;
        double distance = Math.hypot(dx, dy);
        int steps = (int) (distance / STEP_SIZE);

        double xStep = dx / steps;
        double yStep = dy / steps;

        for (int i = 0; i <= steps; i++) {
            int tileX = coordToTile(x1);
            int tileY = coordToTile(y1);
            
            // bounds check (necessary)
            if (tileX < 0 || tileX >= map.getTiles().length || tileY < 0 || tileY >= map.getTiles()[0].length) {
                return false;
            }

            if (map.getTiles()[tileX][tileY] == Utilities.WALL) {
                return false;
            }

            x1 += xStep;
            y1 += yStep;
        }
        return true;
    }

    private int coordToTile(double coordinate) {
        return (int) (coordinate / Utilities.TILE_SIZE);
    }
}
