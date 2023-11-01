package dev.rollczi.litecommands.intellijplugin.features.marker.command.dialog;

public abstract class ParametersListTable {
//    public ParametersListTable() {
//      super(myParametersTable, ChangeSignatureDialogBase.this.getDisposable());
//    }
//
//    @Override
//    protected final JBTableRowEditor getRowEditor(final int row) {
//      JBTableRowEditor editor = getRowEditor(getRowItem(row));
//      editor.addDocumentListener(new JBTableRowEditor.RowDocumentListener() {
//        @Override
//        public void documentChanged(@NotNull DocumentEvent e, int column) {
//          if (String.class.equals(myParametersTableModel.getColumnClass(column))) {
//            myParametersTableModel.setValueAtWithoutUpdate(e.getDocument().getText(), row, column);
//          }
//          updateSignature();
//        }
//      });
//      return editor;
//    }
//
//    @NotNull
//    protected abstract JBTableRowEditor getRowEditor(ParameterTableModelItemBase<ParamInfo> item);
//
//    @Override
//    protected abstract boolean isRowEmpty(int row);
//
//    protected ParameterTableModelItem getRowItem(int row) {
//      return myParametersTable.getItems().get(row);
//    }
  }