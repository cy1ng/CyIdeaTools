package com.cying.ideatools.codemaker.crud.ui;

import com.cying.ideatools.codemaker.crud.CodeTemplate;
import com.cying.ideatools.codemaker.crud.CrudCodeMakerSettings;
import com.cying.ideatools.codemaker.crud.action.creator.IMaker;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName TemplateSelectUI
 * @Description TODO
 * @date 2019-01-31 00:08
 */
public class TemplateSelectUI {
    private JRadioButton fileRadioButton;
    private JRadioButton codeBlockRadioButton;
    private JComboBox codeTemplateSelect;
    private JComboBox fileTemplateSelect;
    private JPanel mypanel;
    private JButton fileTemplateAdd;
    private JButton codeBlockAdd;
    private JList codeBlockTemplateList;
    private JList createFileTemplateList;

    private CrudCodeMakerSettings settings;


    public static void main(String[] args) {
        JFrame frame = new JFrame("ParamFileEditorUi");
        frame.setContentPane(new TemplateSelectUI().mypanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public TemplateSelectUI() {

        this.settings = ServiceManager.getService(CrudCodeMakerSettings.class);
        init();
    }

    private void init() {

        fileRadioButton.setSelected(true);
        // 设置默认下拉列表
        fileTemplateSelect.setModel(new DefaultComboBoxModel(getTemplateList(false).toArray()));
        createFileTemplateList.setModel(new DefaultListModel());
        codeBlockTemplateList.setModel(new DefaultListModel());
        codeBlockRadioButton.setSelected(false);
        codeBlockAdd.setEnabled(false);
        codeTemplateSelect.setEnabled(false);
        fileRadioButton.addActionListener(actionEvent -> {
            // 设置下拉列表
            fileTemplateSelect.setModel(new DefaultComboBoxModel(getTemplateList(false).toArray()));
            fileTemplateAdd.setEnabled(true);
            fileTemplateSelect.setEnabled(true);
            // 将另一个置空
            codeBlockRadioButton.setSelected(false);
            codeBlockAdd.setEnabled(false);
            codeTemplateSelect.setEnabled(false);
        });

        codeBlockRadioButton.addActionListener(actionEvent -> {
            // 设置下拉列表
            codeTemplateSelect.setModel(new DefaultComboBoxModel(getTemplateList(true).toArray()));
            codeBlockAdd.setEnabled(true);
            codeTemplateSelect.setEnabled(true);
            // 将另一个置空
            fileRadioButton.setSelected(false);
            fileTemplateAdd.setEnabled(false);
            fileTemplateSelect.setEnabled(false);
        });

        // 添加事件
        fileTemplateAdd.addActionListener(actionEvent -> {
            ((DefaultListModel) createFileTemplateList.getModel()).addElement(fileTemplateSelect.getSelectedItem());
        });
        codeBlockAdd.addActionListener(actionEvent -> {
            ((DefaultListModel) codeBlockTemplateList.getModel()).addElement(codeTemplateSelect.getSelectedItem());
        });

    }

    public java.util.List<String> getSelTemplateNames() {

        java.util.List<String> templateNames = new ArrayList<>();
        if (createFileTemplateList.isEnabled()) {
            for (int i = 0; i < createFileTemplateList.getModel().getSize(); i++) {
                templateNames.add((String) createFileTemplateList.getModel().getElementAt(i));
            }
        } else {
            for (int i = 0; i < codeBlockTemplateList.getModel().getSize(); i++) {
                templateNames.add((String) codeBlockTemplateList.getModel().getElementAt(i));
            }
        }
        return templateNames;
    }

    public String getTemplateType() {

        if (createFileTemplateList.isEnabled()) {
            return IMaker.FILE_TYPE;
        } else {
            return IMaker.BLOCK_TYPE;
        }
    }


    private java.util.List<String> getTemplateList(boolean codeBlock) {

        Stream<Map.Entry<String, CodeTemplate>> stream = settings.getCodeTemplates().entrySet()
                .stream();
        if (codeBlock) {
            return stream.filter(entry -> entry.getKey().endsWith("code-block"))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        } else {
            return stream.filter(entry -> !entry.getKey().endsWith("code-block"))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }


    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mypanel = new JPanel();
        mypanel.setLayout(new GridLayoutManager(5, 5, new Insets(0, 0, 0, 0), -1, -1));
        fileRadioButton = new JRadioButton();
        fileRadioButton.setText("Gen Files");
        mypanel.add(fileRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mypanel.add(spacer1, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mypanel.add(spacer2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        codeBlockRadioButton = new JRadioButton();
        codeBlockRadioButton.setText("Gen Code Block");
        mypanel.add(codeBlockRadioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        mypanel.add(spacer3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        mypanel.add(spacer4, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        mypanel.add(spacer5, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        mypanel.add(spacer6, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        codeTemplateSelect = new JComboBox();
        mypanel.add(codeTemplateSelect, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileTemplateSelect = new JComboBox();
        mypanel.add(fileTemplateSelect, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileTemplateAdd = new JButton();
        fileTemplateAdd.setText("Add");
        mypanel.add(fileTemplateAdd, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        codeBlockAdd = new JButton();
        codeBlockAdd.setText("Add");
        mypanel.add(codeBlockAdd, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        codeBlockTemplateList = new JList();
        mypanel.add(codeBlockTemplateList, new GridConstraints(3, 2, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        createFileTemplateList = new JList();
        mypanel.add(createFileTemplateList, new GridConstraints(1, 2, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mypanel;
    }

}
