
public class Board {

	Square [][] board;
	int i,j;
	int capture[];
	int help1;
	int wb;
	int ok;
	boolean couldCapture, couldMove;

	Board(){

		board = new Square [8][8];
		i = 0;
		j = 0;
		capture = new int [4];
		help1 = 0;
		wb = 0;
		ok = 0;
		couldCapture = false;
		couldMove = false;

	}

// Begin the board game with the pieces

	void begin(boolean New){

		if(New) {

			i = 0;
			j = 0;

			while(i < board.length){

				while(j < board[i].length){

					board[i][j] = new Square();
					j++;
				}

				j = 0;
				i++;
			}

		}

		i = 0;
		j = 0;

		while(i < board.length){

			while(j < board[i].length){

				if((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)){

					board[i][j].color = "Black";

					if (i < 3){
					
						board[i][j].type = 1;

					}else if(i < 5 && i > 2){

						board[i][j].type = 0;

					}else if(i < 8 && i > 4){

						board[i][j].type = 3;

					}

				}else if((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0)){

					board[i][j].color = "White";
						
						board[i][j].type = 0;

				}

				j++;

			}

			j = 0;
			i++;
		}

		i = 0;

	}

	Square[][] getBoard(){

		return board;
		
	}


	int king(){

		i = 0;
		j = 0;
		ok = 0;
			
		while(j < board[i].length){

			if(board[i][j].type == 3){

				board[i][j].type = 4;
				ok = 1;

			}

			j++;
	
		}

		i = 7;
		j = 0;
		
		while(j < board[i].length){

			if(board[i][j].type == 1){

				board[i][j].type = 2;
				ok = 1;
			}
			j++;

		}

		return ok;

	}

	void moveHelper(int x, int y, int x1, int y1){

		if(board[x1][y1].type == 0){

			help1 = board[x][y].type;
			board[x][y].type = 0;
			board[x1][y1].type = help1;
			couldMove = true;

		}

	}

	void move(int x, int y, int x1, int y1){

		couldMove = false;

		if(board[x][y].type == 1 || board[x][y].type == 2 || board[x][y].type == 4){

			if(x >= 0 && x < 7){

				if(y > 0 && y < 7){

					if(x + 1 == x1){

						if((y + 1 == y1) || (y - 1 == y1)){

							moveHelper(x,y,x1,y1);

						}
					}		

				}else if (y == 0){

					if(x + 1 == x1){

						if(y + 1 == y1){

							moveHelper(x,y,x1,y1);

						}
					}

				}else if(y == 7){

					if(x + 1 == x1){

						if(y - 1 == y1){

							moveHelper(x,y,x1,y1);

						}

					}

				}

				king();

			}

		}

		if(board[x][y].type == 2 || board[x][y].type == 3 || board[x][y].type == 4){

			if(x > 0 && x <= 7){

				if(y > 0 && y < 7){

					if(x - 1 == x1){

						if((y + 1 == y1) || (y - 1 == y1)){

							moveHelper(x,y,x1,y1);
							
						}
					}		

				}else if (y == 0){

					if(x - 1 == x1){

						if(y + 1 == y1){

							moveHelper(x,y,x1,y1);

						}
					}

				}else if(y == 7){

					if(x - 1 == x1){

						if(y - 1 == y1){

							moveHelper(x,y,x1,y1);

						}

					}

				}

				king();

			}

		}

	}

	void ifCaptureHelper(int x, int y, int w1, int w2){

		if(board[x][y].type == 1 || board[x][y].type == 2){

			if (board[x+w1][y+w2].type == 3 || board[x+w1][y+w2].type == 4){

				if(board[x+(w1+w1)][y+(w2+w2)].type == 0){

					if(w1 == 1 && w2 == -1){

						capture[0] = 1;

					}else if(w1 == 1 && w2 == 1){

						capture[1] = 1;

					}else if(w1 == -1 && w2 == -1){

						capture[2] = 1;	

					}else if(w1 == -1 && w2 == 1){

						capture[3] = 1;

					}

				}

			}
		}else if(board[x][y].type == 3 || board[x][y].type == 4){

			if (board[x+w1][y+w2].type == 1 || board[x+w1][y+w2].type == 2){

				if(board[x+(w1+w1)][y+(w2+w2)].type == 0){

					if(w1 == 1 && w2 == -1){

						capture[0] = 1;

					}else if(w1 == 1 && w2 == 1){

						capture[1] = 1;

					}else if(w1 == -1 && w2 == -1){

						capture[2] = 1;	

					}else if(w1 == -1 && w2 == 1){

						capture[3] = 1;

					}

				}

			}
		}

	}

	int [] ifCapture(int x, int y, int wb){

		capture[0] = 0;//left up
		capture[1] = 0;//right up
		capture[2] = 0;//left down
		capture[3] = 0;//right down
		

		if(board[x][y].type == 1 || board[x][y].type == 2){

			if(x < 6 && x > 1){

				if(y == 0 || y == 1){

					ifCaptureHelper(x,y,1,1);

				}else if(y == 6 || y == 7){

					ifCaptureHelper(x,y,1,-1);

				}else if(y > 1 && y < 6){

					ifCaptureHelper(x,y,1,1);
					ifCaptureHelper(x,y,1,-1);

				}

				if(board[x][y].type == 2){

					if(y == 0 || y == 1){

						ifCaptureHelper(x,y,-1,1);

					}else if(y == 6 || y == 7){

						ifCaptureHelper(x,y,-1,-1);

					}else if(y > 1 && y < 6){

						ifCaptureHelper(x,y,-1,1);
						ifCaptureHelper(x,y,-1,-1);

					}

				}

			}else if(x == 0 || x == 1){

				if(y == 0 || y == 1){

					ifCaptureHelper(x,y,1,1);

				}else if(y == 6 || y == 7){

					ifCaptureHelper(x,y,1,-1);

				}else if(y > 1 && y < 6){

					ifCaptureHelper(x,y,1,1);
					ifCaptureHelper(x,y,1,-1);

				}

			}else if((x == 6 || x == 7) && (board[x][y].type == 2)){

				if(y == 0 || y == 1){

					ifCaptureHelper(x,y,-1,1);

				}else if(y == 6 || y == 7){

					ifCaptureHelper(x,y,-1,-1);

				}else if(y > 1 && y < 6){

					ifCaptureHelper(x,y,-1,1);
					ifCaptureHelper(x,y,-1,-1);

				}

			}

		}else if(board[x][y].type == 3 || board[x][y].type == 4){

			if(x < 6 && x > 1){

				if(y == 0 || y == 1){

					ifCaptureHelper(x,y,-1,1);

				}else if(y == 6 || y == 7){

					ifCaptureHelper(x,y,-1,-1);

				}else if(y > 1 && y < 6){

					ifCaptureHelper(x,y,-1,1);
					ifCaptureHelper(x,y,-1,-1);

				}

				if(board[x][y].type == 4){

					if(y == 0 || y == 1){

						ifCaptureHelper(x,y,1,1);

					}else if(y == 6 || y == 7){

						ifCaptureHelper(x,y,1,-1);

					}else if(y > 1 && y < 6){

						ifCaptureHelper(x,y,1,1);
						ifCaptureHelper(x,y,1,-1);

					}

				}

			}else if((x == 0 || x == 1) && (board[x][y].type == 4)){

				if(y == 0 || y == 1){

					ifCaptureHelper(x,y,1,1);

				}else if(y == 6 || y == 7){

					ifCaptureHelper(x,y,1,-1);

				}else if(y > 1 && y < 6){

					ifCaptureHelper(x,y,1,1);
					ifCaptureHelper(x,y,1,-1);

				}

			}else if(x == 6 || x == 7){

				if(y == 0 || y == 1){

					ifCaptureHelper(x,y,-1,1);

				}else if(y == 6 || y == 7){

					ifCaptureHelper(x,y,-1,-1);

				}else if(y > 1 && y < 6){

					ifCaptureHelper(x,y,-1,1);
					ifCaptureHelper(x,y,-1,-1);

				}

			}

		}


		return capture;

	}

	boolean capture(int x, int y, int x1, int y1){

		couldCapture = false;

		if(((x+2) == x1 && (y-2) == y1)){

			board[x1][y1].type = board[x][y].type;
			board[x][y].type = 0;
			board[x+1][y-1].type = 0;
			couldCapture = true;

		}else if(((x+2) == x1 && (y+2) == y1)){

			board[x1][y1].type = board[x][y].type;
			board[x][y].type = 0;
			board[x+1][y+1].type = 0;
			couldCapture = true;

		}else if(((x-2) == x1 && (y-2) == y1)){

			board[x1][y1].type = board[x][y].type;
			board[x][y].type = 0;
			board[x-1][y-1].type = 0;
			couldCapture = true;

		}else if((((x-2) == x1) && ((y+2) == y1))){

			board[x1][y1].type = board[x][y].type;
			board[x][y].type = 0;
			board[x-1][y+1].type = 0;
			couldCapture = true;

		}

		if(king() == 1){

			king();
			return true;

		}

		king();
		return false;

	}

	void printBoard(){

		i = 7;
		j = 0;

		while(i >= 0){

			while(j < board[i].length){

				System.out.print(board[i][j].type + " ");

				j++;
			}

			System.out.println();
			j = 0;
			i--;
		}


	}




	public static void main(String[] args) {


	}


}