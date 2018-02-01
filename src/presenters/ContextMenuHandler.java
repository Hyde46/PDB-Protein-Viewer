package presenters;

import javafx.application.Platform;
import presenters.commandManagers.CommandManager;
import utils.MyRotateTransition;

class ContextMenuHandler {

    private PDBPresenter presenter;

    void setupContextMenu(){
        PDBConnector connector = presenter.getConnector();
        CommandRegister commands = presenter.getCommandRegister();
        CommandManager manager = presenter.getCommandRegister().getCommandManager();
        registerListeners(connector,commands);
        setupProperties(connector,manager);
    }

    private void registerListeners(PDBConnector connector,CommandRegister commands){
        //File Menu
        registerFileMenuListeners(connector, commands);
        //Edit
        registerEditMenuListeners(connector, commands);
        //Tools
        registerToolMenuListeners(connector);
        //View
        registerViewMenuListeners(connector, commands);
        //second button bar
        registerAlternativeBarListeners(connector, commands);
        registerViewBindings(connector);
    }

    private void registerViewBindings(PDBConnector connector){
        //color mode
        connector.getStructureTypeButton().selectedProperty().bindBidirectional(connector.getStructureColorButton().selectedProperty());
        connector.getAcidTypeButton().selectedProperty().bindBidirectional(connector.getTypeColorButton().selectedProperty());
        //render mode
        connector.getMainAtomButton().selectedProperty().bindBidirectional(connector.getMainFormButton().selectedProperty());
        connector.getSpaceFillingButton().selectedProperty().bindBidirectional(connector.getSpaceFormButton().selectedProperty());
        connector.getBackBoneButton().selectedProperty().bindBidirectional(connector.getBackboneFormButton().selectedProperty());
        connector.getRibbonButton().selectedProperty().bindBidirectional(connector.getRibbonFormButton().selectedProperty());
        connector.getCartoonButton().selectedProperty().bindBidirectional(connector.getCartoonFormButton().selectedProperty());
    }

    private void registerAlternativeBarListeners(PDBConnector connector, CommandRegister commands) {
        connector.getLoadButton().setOnAction(e->      commands.executeLoadFile());
        connector.getLoadOnlineButton().setOnAction(e->commands.executeShowList());
        connector.getShowChartsButton().setOnAction( e -> presenter.getUiHandler().showCharts());
        connector.getNodeSizeSlider().valueProperty().bindBidirectional(presenter.getGraphView().currentNodesScalingProperty());
        setupBondSizeSliderProperty(connector);
    }

    private void registerViewMenuListeners(PDBConnector connector, CommandRegister commands) {
        connector.getStructureColorButton().setOnAction(e-> commands.executeColorStructureMode());
        connector.getTypeColorButton().setOnAction(e->      commands.executeColorTypeMode());
        connector.getMainFormButton().setOnAction(e->       commands.executeMainAtomFormRenderMode());
        connector.getSpaceFormButton().setOnAction(e->      commands.executeSpaceFillingFormRenderMode());
        connector.getBackboneFormButton().setOnAction(e->   commands.executeBackboneFormRenderMode());
        connector.getRibbonFormButton().setOnAction(e->     commands.executeRibbonFormRenderMode());
        connector.getCartoonFormButton().setOnAction(e->    commands.executeCartoonFormRenderMode());

        connector.getStructureTypeButton().setOnAction(e-> commands.executeColorStructureMode());
        connector.getAcidTypeButton().setOnAction(e->      commands.executeColorTypeMode());
        connector.getMainAtomButton().setOnAction(e->       commands.executeMainAtomFormRenderMode());
        connector.getSpaceFillingButton().setOnAction(e->      commands.executeSpaceFillingFormRenderMode());
        connector.getBackBoneButton().setOnAction(e->   commands.executeBackboneFormRenderMode());
        connector.getRibbonButton().setOnAction(e->     commands.executeRibbonFormRenderMode());
        connector.getCartoonButton().setOnAction(e->    commands.executeCartoonFormRenderMode());
    }

    private void registerToolMenuListeners(PDBConnector connector) {
        connector.getAutoRotateButton().setOnAction(e-> ((MyRotateTransition)presenter.getUiHandler().getAutoRotateAnimation()).toggleAutoRotate());
        connector.getBgColorButton().setOnAction( e -> presenter.getUiHandler().handleBGColorToggle());
        connector.getShowChartsItem().setOnAction( e -> presenter.getUiHandler().showCharts());
        connector.getResetButton().setOnAction(e-> presenter.handleResetTransform());
    }

    private void registerEditMenuListeners(PDBConnector connector, CommandRegister commands) {
        connector.getUndoButton().setOnAction(e->      commands.undo());
        connector.getRedoButton().setOnAction(e->      commands.redo());
        connector.getSelectAllButton().setOnAction(e-> commands.executeSelectAll());
        connector.getSelectNoneButton().setOnAction(e->commands.executeSelectNone());
    }

    private void registerFileMenuListeners(PDBConnector connector, CommandRegister commands) {
        connector.getLoadFile().setOnAction(e->commands.executeLoadFile());
        connector.getShowList().setOnAction(e->commands.executeShowList());
        connector.getCloseButton().setOnAction(e-> Platform.exit());
    }

    private void setupProperties(PDBConnector connector, CommandManager manager){
        presenter.getGraphModel().isEmptyProperty().addListener( e -> updateEmptyProteinText() );
        setupDisableProperties(connector,manager);
        connector.getUndoButton().textProperty().bind(manager.undoNameProperty());
        connector.getRedoButton().textProperty().bind(manager.redoNameProperty());
        connector.getAutoRotateButton().textProperty().bind(((MyRotateTransition)presenter.getUiHandler().getAutoRotateAnimation()).toggleDescriptionProperty());
        connector.getAutoTransformCheckBox().selectedProperty().bindBidirectional(((MyRotateTransition) presenter.getUiHandler().getAutoRotateAnimation()).isAutoRotatingProperty());
    }

    private void setupDisableProperties(PDBConnector connector, CommandManager manager){
        connector.getUndoButton().disableProperty().bind(manager.canUndoProperty().not());
        connector.getRedoButton().disableProperty().bind(manager.canRedoProperty().not());
        connector.getShowChartsButton().disableProperty().bind(presenter.getGraphModel().isEmptyProperty());
        connector.getShowChartsItem().disableProperty().bind(presenter.getGraphModel().isEmptyProperty());
        connector.getNodeSizeSlider().disableProperty().bind(presenter.getGraphModel().isEmptyProperty());
        connector.getBondSizeSlider().disableProperty().bind(presenter.getGraphModel().isEmptyProperty());
        connector.getResetButton().disableProperty().bind(presenter.getGraphModel().isEmptyProperty());

    }

    private void setupBondSizeSliderProperty(PDBConnector connector){
        connector.getBondSizeSlider().setOnMouseReleased(event ->
                presenter.getGraphView().currentBondScalingProperty().setValue(connector.getBondSizeSlider().getValue()));
    }

    void setPresenter(PDBPresenter p){
        this.presenter = p;
    }

    private void updateEmptyProteinText(){
        presenter.getConnector().getLogoView().setVisible(presenter.getGraphModel().isEmptyProperty().getValue());
    }
}
