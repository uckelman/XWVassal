package mic;

import VASSAL.build.module.PrototypeDefinition;
import VASSAL.build.module.PrototypesContainer;
import VASSAL.build.widget.PieceSlot;
import VASSAL.counters.GamePiece;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static mic.Util.logToChat;

/**
 * Created by mjuneau on 6/8/18.
 */
public class VassalXWSPilotPieces2e {

    private static Map<String, String> actionLayers = ImmutableMap.<String, String>builder()
            .put("","1")
            .put("Focus","2")
            .put("Lock","3")
            .put("Boost","4")
            .put("Evade","5")
            .put("Barrel Roll","6")
            .put("Cloak","7")
            .put("SLAM","8")
            .put("Rotate Arc","9")
            .put("Reinforce","10")
            .put("Reload","11")
            .put("Jam","12")
            .build();


    private PieceSlot pilotCard;
    private PieceSlot dial;
    private PieceSlot movementCard;
    private List<VassalXWSPilotPieces2e.Upgrade> upgrades = new ArrayList<Upgrade>();
    private List<VassalXWSPilotPieces2e.Condition> conditions = new ArrayList<Condition>();
    private PieceSlot movementStrip;
    private PieceSlot openDial;
    private XWS2Pilots shipData;
    private XWS2Pilots.Pilot2e pilotData;
    private Integer shipNumber = null;
    private Map<Tokens2e, PieceSlot> tokens = Maps.newHashMap();
    private PieceSlot ship;

    public VassalXWSPilotPieces2e() {

    }

    public VassalXWSPilotPieces2e(VassalXWSPilotPieces2e pieces) {
        this.dial = pieces.dial;
        this.movementCard = pieces.movementCard;
        this.movementStrip = pieces.movementStrip;
        this.openDial = pieces.openDial;
        this.pilotCard = pieces.pilotCard;
        this.ship = pieces.ship;
        this.shipData = pieces.shipData;
        this.pilotData = pieces.pilotData;
    }

    public List<VassalXWSPilotPieces2e.Condition> getConditions() {
        return this.conditions;
    }

    public PieceSlot getShip() {
        return ship;
    }

    public void setShip(PieceSlot ship) {
        this.ship = ship;
    }


    public PieceSlot getPilotCard() {
        return pilotCard;
    }

    public void setPilotCard(PieceSlot pilotCard) {
        this.pilotCard = pilotCard;
    }

    public PieceSlot getDial() {
        return dial;
    }

    public void setDial(PieceSlot dial) {
        this.dial = dial;
    }

    public PieceSlot getMovementCard() {
        return movementCard;
    }

    public void setMovementCard(PieceSlot movementCard) {
        this.movementCard = movementCard;
    }

    public List<VassalXWSPilotPieces2e.Upgrade> getUpgrades() {
        return upgrades;
    }

    public Map<Tokens2e, PieceSlot> getTokens() {
        return tokens;
    }

    public List<GamePiece> getTokensForDisplay() {
        List<GamePiece> tokenPieces = Lists.newArrayList();
        for (Tokens2e token : tokens.keySet()) {
            GamePiece piece = Util.newPiece(tokens.get(token));
            if (token == Tokens2e.lock && pilotData != null) {
                piece.setProperty("ID", getDisplayPilotName());
            }
            tokenPieces.add(piece);
        }
        return tokenPieces;
    }

    public void setMovementStrip(PieceSlot movementStrip) {
        this.movementStrip = movementStrip;
    }

    public void setOpenDial(PieceSlot openDial) {
        this.openDial = openDial;
    }

    public PieceSlot getMovementStrip() {
        return movementStrip;
    }

    public PieceSlot getOpenDial() {
        return openDial;
    }

    public void setShipData(XWS2Pilots shipData) {
        this.shipData = shipData;
    }

    public void setShipNumber(Integer number) {
        this.shipNumber = number;
    }

    public Integer getShipNumber()
    {
        if(shipNumber == null)
        {
            return null;
        }
        return shipNumber;
    }

    public void setPilotData(XWS2Pilots.Pilot2e pilotData) {
        this.pilotData = pilotData;
    }

    public GamePiece clonePilotCard() {
        GamePiece piece = Util.newPiece(this.pilotCard);
        if (shipNumber != null && shipNumber > 0) {
            piece.setProperty("Pilot ID #", shipNumber);
        } else {
            piece.setProperty("Pilot ID #", "");
        }

        if(this.pilotCard.getConfigureName().startsWith("Stem"))
        {
            // this is a stem card = fill it in
            piece.setProperty("Ship Type",this.shipData.getName());
            piece.setProperty("Pilot Name",this.pilotData.getName());
        }
        return piece;
    }

    public GamePiece cloneDial(String fullShipName) {
        GamePiece piece = Util.newPiece(this.dial);

        setPilotShipName(piece, fullShipName);

        return piece;
    }

    private GamePiece addPrototypeToPiece(GamePiece piece, String prototypeName)
    {
        logToChat("Adding prototype "+prototypeName+ " to piece");
        PrototypeDefinition protoDef = PrototypesContainer.getPrototype(prototypeName);
        if(protoDef == null)
        {
            logToChat("protoDef is null");
        }
        protoDef.setPiece(piece);
        return piece;
    }

    private void setPilotShipName(GamePiece piece, String fullShipName) {
        if (pilotData != null) {
            piece.setProperty("Pilot Name", getDisplayShipName(fullShipName));
        }
        piece.setProperty("Craft ID #", getDisplayPilotName());
    }

    private String getDisplayPilotName() {
        String pilotName = "";
        if (pilotData != null) {
            pilotName = Acronymizer.acronymizer(
                    this.pilotData.getName(),
                    this.pilotData.isUnique(),
                    this.shipData.hasSmallBase());
        }

        if (shipNumber != null && shipNumber > 0) {
            pilotName += " " + shipNumber;
        }
        return pilotName;
    }

    private String getDisplayShipName(String fullShipName) {
        String shipName = "";
        if (pilotData != null) {
            shipName = Acronymizer.acronymizer(
                    fullShipName,
                    this.pilotData.isUnique(),
                    this.shipData.hasSmallBase());
        }

        return shipName;
    }

    public XWS2Pilots getShipData() {
        return shipData;
    }

    public XWS2Pilots.Pilot2e getPilotData() {
        return pilotData;
    }

    public static class Condition {
        private String xws;
        private String name;
        private PieceSlot pieceSlot;
        private MasterConditionData.ConditionData conditionData;

        public Condition(PieceSlot pieceSlot, String xws, String name)
        {
            this.xws = xws;
            this.pieceSlot = pieceSlot;
            this.name = name;
        }

        public String getXws()
        {
            return this.xws;
        }
        public PieceSlot getPieceSlot()
        {
            return this.pieceSlot;
        }
        public MasterConditionData.ConditionData getConditionData()
        {
            return this.conditionData;
        }

        public void setConditionData(MasterConditionData.ConditionData conditionData)
        {
            this.conditionData = conditionData;
        }
        public GamePiece cloneGamePiece() {
            return Util.newPiece(pieceSlot);
        }
    }

    public static class Upgrade {
        private String xwsName;
        private PieceSlot pieceSlot;
        private MasterUpgradeData.UpgradeData upgradeData;


        public Upgrade(String xwsName, PieceSlot pieceSlot) {
            this.xwsName = xwsName;
            this.pieceSlot = pieceSlot;
        }

        public String getXwsName() {
            return xwsName;
        }

        public PieceSlot getPieceSlot() {
            return this.pieceSlot;
        }

        public MasterUpgradeData.UpgradeData getUpgradeData() {
            return upgradeData;
        }

        public void setUpgradeData(MasterUpgradeData.UpgradeData upgradeData) {
            this.upgradeData = upgradeData;
        }

        public GamePiece cloneGamePiece() {
            return Util.newPiece(pieceSlot);
        }
    }
}