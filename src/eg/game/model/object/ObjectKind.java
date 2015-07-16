package eg.game.model.object;

/**
 * Represents the kind of an {@link Object}.
 * 
 * @author Bubletan <https://github.com/Bubletan>
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Maxi <http://www.rune-server.org/members/maxi/>
 */
public enum ObjectKind {
    
    STRAIGHT_WALL(0),
    DIAGONAL_CORNER_WALL(1),
    ENTIRE_WALL(2),
    WALL_CORNER(3),
    STRAIGHT_INSIDE_WALL_DECORATION(4),
    STRAIGHT_OUTSIDE_WALL_DECORATION(5),
    DIAGONAL_OUTSIDE_WALL_DECORATION(6),
    DIAGONAL_INSIDE_WALL_DECORATION(7),
    DIAGONAL_INTERIOR_WALL_DECORATION(8),
    DIAGONAL_WALL(9),
    GENERAL_PROP(10),
    WALKABLE_PROP(11),
    STRAIGHT_SLOPED_ROOF(12),
    DIAGONAL_SLOPED_ROOF(13),
    DIAGONAL_SLOPED_CONNECTING_ROOF(14),
    STRAIGHT_SLOPED_CORNER_CONNECTING_ROOF(15),
    STRAIGHT_SLOPED_CORNER_ROOF(16),
    STRAIGHT_FLAT_TOP_ROOF(17),
    STRAIGHT_BOTTOM_EDGE_ROOF(18),
    DIAGONAL_BOTTOM_EDGE_CONNECTING_ROOF(19),
    STRAIGHT_BOTTOM_EDGE_CONNECTING_ROOF(20),
    STRAIGHT_BOTTOM_EDGE_CONNECTING_CORNER_ROOF(21),
    GROUND_PROP(22);
    
    private final int value;
    
    private ObjectKind(int value) {
        this.value = value;
    }
}
