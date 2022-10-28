public class ground{
  public int top;
  public int bottom;
  public int left;
  public int right;
  //sets the collision box for the ground tiles
  public void setCollision(int x1,int y1,int x2,int y2){
    if(y1 > y2){
      top = y2;
      bottom = y1;
    }
    else{
      top = y1;
      bottom = y2;
    }
    if(x1 > x2){
      left = x2;
      right = x1;
    }
    else{
      left = x1;
      right = x2;
    }
  }
  //checks if the player is colliding with the collision box and checks if the player is standing on ground(for jump checks)
  public void checkCollision(player p){
    if (left < p.right && right > p.left && top < p.bottom && bottom > p.top){
      if(p.right > left && p.left < left && p.bottom > top && p.top < top){
        if(p.right - left > p.bottom - top){
          p.setpos(p.posx, top - p.height / 2);
          p.setvel(p.velx, 0);
          p.isOnGround = true;
        }
        if(p.right - left < p.bottom - top){
          p.setpos(left - p.width / 2, p.posy);
          p.setvel(0 ,p.vely);
        }
      }
      else if(p.right > left && p.left < left && bottom > p.top && p.bottom > bottom){
        if(p.right - left > bottom - p.top){
          p.setpos(p.posx, bottom + p.height / 2);
          p.setvel(p.velx, 0);
        }
        if(p.right - left < bottom - p.top){
          p.setpos(left - p.width / 2, p.posy);
          p.setvel(0 ,p.vely);
        }
      }
      else if(right > p.left && p.right > right && p.bottom > top && p.top < top){
        if(right - p.left > p.bottom - top){
          p.setpos(p.posx, top - p.height / 2);
          p.setvel(p.velx, 0);
          p.isOnGround = true;
        }
        if(right - p.left < p.bottom - top){
          p.setpos(right + p.width / 2, p.posy);
          p.setvel(0 ,p.vely);
        }
      }
      else if(right > p.left && p.right > right && bottom > p.top && p.bottom > bottom){
        if(right - p.left > bottom - p.top){
          p.setpos(p.posx, bottom + p.height / 2);
          p.setvel(p.velx, 0);
        }
        if(right - p.left < bottom - p.top){
          p.setpos(right + p.width / 2, p.posy);
          p.setvel(0 ,p.vely);
        }
      }
      else if(p.right > left && p.left < left){
        p.setpos(left - p.width / 2, p.posy);
        p.setvel(0 ,p.vely);
      }
      else if(right > p.left && p.right > right){
        p.setpos(right + p.width / 2, p.posy);
        p.setvel(0 ,p.vely);
      }
      else if(p.bottom > top && p.top < top){
        p.setpos(p.posx, top - p.height / 2);
        p.setvel(p.velx, 0);
        p.isOnGround = true;
      }
      else if(bottom > p.top && p.bottom > bottom){
        p.setpos(p.posx, bottom + p.height / 2);
        p.setvel(p.velx, 0);
      }
    }
  }
}
