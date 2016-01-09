package fiveinarow.Players;

import fiveinarow.Game.Game;
import java.awt.Point;
import java.util.*;

/**
 * 
 */

/**
 * @author Johan Rydh
 *
 */
public class AIJohan extends Player implements IAI
{
	boolean aggresive;
	Random rand = new Random();

	public AIJohan(int id, Game game, boolean aggresive) {
		super(id, game);

		this.aggresive = aggresive;
	}

	@Override
	public void playRound() {
		Point p = aiGetNext(game.getBoard(), ID, ID == 1 ? 2 : 1, 0, aggresive);

		while (p == null || 0 != game.getBoard()[p.y][p.x])
		{
			p = new Point(rand.nextInt(game.getBoard()[0].length), rand.nextInt(game.getBoard().length));
		}

		game.setTile(p.y, p.x, ID);
		game.incrementRoundCount();
		game.nextPlayer();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		final char[] marks = {' ', '-', 'A'};
		int markEmpty 	= 0;
		int markPlayer 	= 1;
		int markAi		 	= 2;

		int[][] board = new int[25][25];
		Random rand = new Random();
		
		for (int i = 0;; i += 2)
		{
			Point p = null;

			System.out.println("-------------------");
			System.out.println("Pl: " + i);
			p = aiGetNext(board, markPlayer, markAi, markEmpty, false);
			
			while (p == null || markEmpty != board[p.y][p.x])
			{
				p = new Point(rand.nextInt(board[0].length), rand.nextInt(board.length));
			}
			board[p.y][p.x] = markPlayer;
			printBoard(board, marks);
			if (!search(board, 5, markPlayer, 0).isEmpty())
			{
				System.out.println("Winner");
				return;
			}
			if (search(board, 5, markEmpty, 0).isEmpty())
			{
				System.out.println("Draw");
				return;
			}

			System.out.println("-------------------");
			System.out.println("AI: " + (i + 1));
			p = aiGetNext(board, markAi, markPlayer, markEmpty, false);
			board[p.y][p.x] = markAi;
			printBoard(board, marks);
			if (!search(board, 5, markAi, 0).isEmpty())
			{
				System.out.println("Winner");
				return;
			}
			if (search(board, 5, markEmpty, 0).isEmpty())
			{
				System.out.println("Draw");
				return;
			}
		}
	}
	
	private static void printBoard(int[][] board, char[] marks)
	{		
		System.err.flush();
		System.out.flush();
		System.out.print(" ");
		for (int x = 0; x < board[0].length; x++)
		{
			System.out.print("_" + (x % 10));
		}
		System.out.println("_");
		for (int y = 0; y < board.length; y++)
		{
			System.out.print(y % 10);
			for (int x = 0; x < board[0].length; x++)
			{
				System.out.print(" " + marks[Math.min(board[y][x], marks.length - 1)]);
			}
			System.out.println(" |");
		}
		System.out.print(" -");
		for (int x = 0; x < board[0].length; x++)
		{
			System.out.print("--");
		}
		System.out.println();
		System.out.flush();
	}

	private enum Direction
	{
		RIGTH, DOWN, DOWN_RIGHT, DOWN_LEFT;

		void movePoint(Point p)
		{
			movePoint(p, 1);
		}

		void movePoint(Point p, int offset)
		{
			switch (this)
			{
			case DOWN:
				p.y += offset;
				break;
			case DOWN_LEFT:
				p.x -= offset;
				p.y += offset;
				break;
			case DOWN_RIGHT:
				p.x += offset;
				p.y += offset;
				break;
			case RIGTH:
				p.x += offset;
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	private static class Line
	{
		private final Point			origin_;
		private final Direction	direction_;

		public Line(Point p, Direction dir)
		{
			origin_ 		= p;
			direction_ 	= dir;
		}

		public Point getOrigin()
		{
			return origin_.getLocation();
		}
		
		public Point getPoint(int offset)
		{
			Point p = getOrigin();
			getDirection().movePoint(p, offset);
			return p;
		}

		public Direction getDirection()
		{
			return direction_;
		}
		
		@Override
		public String toString()
		{
			return "Line (" + origin_.y + "," + origin_.x + ") " + direction_;
		}
	}

	/**
	 * Returns the mark type of a given position, if the coordinate isn't within
	 * the board the <defValue> value will be returned instead. The position is
	 * based on a base position with a offset and a direction.
	 * @param[in] board 				Game board matrix.
	 * @param[in] offset				Offset from base position.
	 * @param[in] defaultValue 	Value to return if outside the board.
	 * @return Mark type at the given point.
	 */
	private static int getMark(int board[][], Line line, int offset, int defaultValue)
	{
		Point p = line.getPoint(offset);

		if (p.x < 0 || p.y < 0 || p.x >= board[0].length || p.y >= board.length)
		{
			return defaultValue;
		}

		return board[p.y][p.x];
	}

	/**
	 * Searches for lines where at least <numMarks> of five marks, is of the type
	 * <searchFor>. Lines containing any mark of the <avoid> type will be ignored.
	 * @param[in] board 		Game board matrix.
	 * @param[in] numMarks 	Required number of right marks within a line (of length five).
	 * @param[in] searchFor Mark type to find.
	 * @param[in] avoid 		Mark type that must not be in any line.
	 * @return List of found lines.
	 */
	private static List<Line> search(
			final int board[][],
			final int numMarks,
			final int searchFor,
			final int avoid)
	{
		List<Line> list = new LinkedList<Line>();

		for (int y = 0; y < board.length; y++)
		{
			for (int x = 0; x < board[0].length; x++)
			{
				for (Direction dir : Direction.values())
				{
					int matches = 0; // Number of matching marks.
					Point p 		= new Point(x, y);

					for (int offset = 0; offset < 5; offset++)
					{
						// Check state of the next five marks in the line with current direction.

						if (p.x < 0 || p.y < 0 || p.x >= board[0].length || p.y >= board.length)
						{
							// A coordinate, and the line, is out of range, no one could win.
							matches = -1;
							break;
						}

						final int value = board[p.y][p.x];

						if (value == searchFor)
						{
							matches++; // Increase the number of matching marks.
						}
						else if (value == avoid)
						{
							matches = -1;
							break; // <Avoid> mark found, ignore this line.
						}

						dir.movePoint(p);
					}

					// Line is verified <marks> contains the number of matches
					// (unless the line is ignored, then it'll be zero)

					// Check if there are enough matches.
					if (matches >= numMarks)
					{
						list.add(new Line(new Point(x, y), dir));
					}
				}
			}
		}
		return list;
	}

	/**
	 * Searches for the locations with the selected mark type, in a given line
	 * NOTE: The line (of length five) has to be within the board (No check is
	 * done)
	 * @param[in] board		Game board matrix.
	 * @param[in] line		Line to search.
	 * @param[in] mark		Mark to search for.
	 * @return List of points of type mark.
	 */
	private static List<Point> findMark(int[][] board, Line line, int mark)
	{
		List<Point> result = new LinkedList<Point>();

		Point p = line.getOrigin();

		for (int i = 0; i < 5; i++)
		{

			if (board[p.y][p.x] == mark)
			{
				result.add(p.getLocation());
			}

			// Update coordinates
			line.getDirection().movePoint(p);
		}

		return result;
	}
	
	private static class SearchConfig
	{
		public final int 		 lineLength_;
		public final boolean checkMine_;
		
		public SearchConfig(final int lineLength, final boolean checkMine)
		{
			lineLength_ = lineLength;
			checkMine_  = checkMine;
		}
		
		@Override
		public String toString()
		{
			return "SearchConfig (" + lineLength_ +", " + checkMine_ + ")";
		}
	}

	/**
	 * Main AI function
	 *
	 * Strategy: 1. See if I can win (AI: 4 -> 5) 2. See if I (really) can stop
	 * all your 4's (Pl: 4 -> [4]) -. Don't care A. Really stop B. Else try
	 * stop/do nothing 3. See if I can get a good chance to win (AI: 3 -> 4) A:
	 * That not could be blocked B: All other locations 4. See if I can stop your
	 * good chances to win A: (Pl: 3 -> {3} -> 4 -> [4]) B: (Pl: 3 -> [3])
	 * 
	 * Select the positions with most matches at the highest priority level
	 * available, if there are more then one position at top, the next priority
	 * level should be consulted, if there remain more than one position at the
	 * end, the choice will be random. (Does not apply to priority level 1.)
	 * 
	 * I means this AI; You means the opponent [] means blocked line; {} half
	 * blocked line (under control)
	 */
	public static Point aiGetNext(
			final int[][] board, 
			final int _AI_MY,
			final int _AI_PLAYER, 
			final int _AI_EMPTY,
			final boolean offensive)
	{
		Map<Integer, SearchConfig> prioMap = new HashMap<Integer, SearchConfig>();

		// Search all line lengths, if defensive starting with the longest and mine in order to try to win.
		if(!offensive){
			prioMap.put(0, new SearchConfig(4, true));
			prioMap.put(1, new SearchConfig(4, false));
			prioMap.put(2, new SearchConfig(3, false));
			prioMap.put(3, new SearchConfig(2, false));
			prioMap.put(4, new SearchConfig(3, true));
			prioMap.put(5, new SearchConfig(1, false));
			prioMap.put(6, new SearchConfig(2, true));
			prioMap.put(7, new SearchConfig(1, true));
			prioMap.put(8, new SearchConfig(0, true));
		}else{
			prioMap.put(0, new SearchConfig(4, true));
			prioMap.put(1, new SearchConfig(4, false));
			prioMap.put(2, new SearchConfig(3, true));
			prioMap.put(3, new SearchConfig(3, false));
			prioMap.put(4, new SearchConfig(2, true));
			prioMap.put(5, new SearchConfig(2, false));
			prioMap.put(6, new SearchConfig(1, true));
			prioMap.put(7, new SearchConfig(1, false));
			prioMap.put(8, new SearchConfig(0, true));
		}
		
		for (int i = 0; i < prioMap.size(); ++i)
		{
			SearchConfig sc = prioMap.get(i);
			int lineLength  = sc.lineLength_;

			final int searchFor =  sc.checkMine_ ? _AI_MY : _AI_PLAYER;
			final int avoidMark = !sc.checkMine_ ? _AI_MY : _AI_PLAYER;
			
			List<Point> possibilities = new LinkedList<Point>();
			
			// Get all possible winning lines.
			for (Line line : search(board, lineLength, searchFor, avoidMark))
			{				
				// Get all empty positions.
				for (Point empty : findMark(board, line, _AI_EMPTY))
				{
					possibilities.add(empty);
				}
			}
			
			// Try to get the best point
			if (!possibilities.isEmpty())
			{
				int[][] counts 	= new int[board.length][board[0].length];
				int 		max  		= -1;
				Point		maxP 		= null;
				
				for (Point p : possibilities)
				{
					counts[p.y][p.x] += 1;
					
					if (counts[p.y][p.x] > max)
					{
						max  = counts[p.y][p.x];
						maxP = p;
					}
				}
				System.out.println("Strategy: " + sc);
				printBoard(counts, new char[] {' ', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+'});
				return maxP;
			}
		}
		
		return null;
		
//		// See if I have four marks in a line.
//		for (Line line : search(board, 4, _AI_MY, _AI_PLAYER))
//		{
//			List<Point> points = findMark(board, line, _AI_EMPTY);
//
//			if (!points.isEmpty()) // I WIN NOW!
//			{
//				// There could only be one empty position per line, use the first found
//				// position.
//				return points.get(0);
//			}
//		}
//
//		// See if you have four in a line.
//		for (Line line : search(board, 4, _AI_PLAYER, _AI_MY))
//		{
//			List<Point> points = findMark(board, line, _AI_EMPTY);
//
//			if (!points.isEmpty())
//			{
//				// There could only be one empty position per line, use the first found
//				// position.
//				return points.get(0);
//			}
//		}
//
//		// A AI
//		// a !AI
//		// P Player
//		// p !Player
//		// - Empty
//		// _ !Empty
//		// X AI_SET
//		// ~ One of ~ is empty
//		// . ... (not defined)
//
//		// See if I have any good three combination.
//		for (Line line : search(board, 3, _AI_MY, _AI_PLAYER))
//		{
//			// Old strategy: (-AAA- | ~A-AA~ | ~AA-A~)
//
//			int[] marks = new int[7];
//			for (int i = -1; i < 6; i++) // L�ser in mark�rer p� fem-raden +
//																		// (f�reg�ende+n�sta) pos
//			{
//				// Default = _AI_PLAYER f�r att enkelt hantera planens kant
//				marks[i + 1] = getMark(board, line, i, _AI_PLAYER);
//			}
//			
//			Point p = line.getOrigin();
//			
//			for (int i = 1; i < 4; i++)
//			{
//				if (marks[i - 1] == _AI_EMPTY
//				&&  marks[i + 0] == _AI_MY
//				&&  marks[i + 1] == _AI_MY
//				&&  marks[i + 2] == _AI_MY
//				&&  marks[i + 3] == _AI_EMPTY)
//				{
//					// AI har tre i rad .XAAA-.
//					line.getDirection().movePoint(p, i - 2); // REL = (i-1)-1
//					return p;
//				}
//				else if (i < 3)
//				{
//					if (marks[i + 0] == _AI_MY
//					&&  marks[i + 1] == _AI_EMPTY
//					&&  marks[i + 2] == _AI_MY
//					&&  marks[i + 3] == _AI_MY
//					&& (marks[i - 1] == _AI_EMPTY || marks[i + 4] == _AI_EMPTY))
//					{
//						// AI har tre i rad .~AXAA~.
//						line.getDirection().movePoint(p, i); // REL = (i-1)+1
//						return p;
//					}
//					else if (marks[i + 0] == _AI_MY
//							 &&  marks[i + 1] == _AI_MY
//							 &&  marks[i + 2] == _AI_EMPTY
//							 &&  marks[i + 3] == _AI_MY
//							 && (marks[i - 1] == _AI_EMPTY || marks[i + 4] == _AI_EMPTY))
//					{
//						// AI har tre i rad .~AAXA~.
//						line.getDirection().movePoint(p, i + 1); // REL = (i-1)+2
//						return p;						
//					}
//				}
//			}
//		}
	}
}
