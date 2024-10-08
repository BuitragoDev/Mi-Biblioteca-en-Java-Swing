package GUI;

import Logica.Conexion;
import Logica.Funciones;
import Logica.DateNumericDocumentFilter;
import Logica.Libro;
import Logica.NumericDocumentFilter;
import Persistencia.LibroDAO;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class VentanaPrincipal extends javax.swing.JFrame {

    private String rutaImagenSeleccionada;
    private String nombreArchivoNuevo;
    private String nombrePortada; // Variable para almacenar el nombre de la portada
    DefaultTableModel modelo; // Creación del modelo de la tabla.

    // MÉTODOS PARA LOS LISTENERS ------------------------------------------------------------------------------------------------------
    private void agregarListeners() {
        // Agregar listeners a todos los campos
        txtISBN.getDocument().addDocumentListener(new CamposListener());
        txtTitulo.getDocument().addDocumentListener(new CamposListener());
        txtAutor.getDocument().addDocumentListener(new CamposListener());
        txtGenero.getDocument().addDocumentListener(new CamposListener());
        txtFechaPublicacion.getDocument().addDocumentListener(new CamposListener());
        txtPaginas.getDocument().addDocumentListener(new CamposListener());
        txtEditorial.getDocument().addDocumentListener(new CamposListener());
        txtSinopsis.getDocument().addDocumentListener(new CamposListener());

        // Verificar manualmente si se añade una imagen en lblPortada
        lblPortada.addPropertyChangeListener("icon", evt -> verificarCamposYActivarBoton());

        // Añadir DocumentListener a los JTextField
        txtFechaFinEditar.getDocument().addDocumentListener(new CamposListener());

    }

    private class CamposListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            verificarCamposYActivarBoton();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            verificarCamposYActivarBoton();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            verificarCamposYActivarBoton();
        }

    }
    // ---------------------------------------------------------------------------------------------------------------------------------

    public VentanaPrincipal() {
        initComponents();

        // Agregar los listeners para habilitar el botón cuando los campos estén completos
        agregarListeners();

        // Crear carpeta "portadas" y guardar una copia de la portada por defecto.
        Funciones funciones = new Funciones();
        funciones.crearCarpetaYCopiarImagen();

        // MODIFICACIÓN DE TITULO DE VENTANA Y FAVICON ----------------------------------------------------------------------------------     
        setTitle("Mi Biblioteca 1.0 by Antonio Buitrago"); // Cambiar el título de la ventana
        ImageIcon icono = new ImageIcon(getClass().getClassLoader().getResource("favicon.png")); // Establecer el icono de la ventana
        setIconImage(icono.getImage());
        // ------------------------------------------------------------------------------------------------------------------------------

        // CÓDIGO PARA MOSTRAR UN SONIDO AL HACER CLIC EN LOS BOTONES -------------------------------------------------------------------
        Funciones misFunciones = new Funciones();
        misFunciones.agregarSonidoABotones(panelBotones);
        misFunciones.agregarSonidoABotones(panelVerTodos);
        misFunciones.agregarSonidoABotones(panelAgregar);
        misFunciones.agregarSonidoABotones(panelBotonPDF);
        misFunciones.agregarSonidoABotones(panelEditar5);
        misFunciones.agregarSonidoABotones(panelAgregar5);
        // ------------------------------------------------------------------------------------------------------------------------------

        // CONFIGURACIONES AL CARGAR LA APLICACIÓN --------------------------------------------------------------------------------------
        panelAgregar.setVisible(false);
        panelEditar.setVisible(false);
        panelVerTodos.setVisible(true);
        // ------------------------------------------------------------------------------------------------------------------------------

        // MODIFICACIONES EN LA TABLA ---------------------------------------------------------------------------------------------------
        String[] titulos = {"ID", "ISBN", "Titulo", "Autor", "Género", "Páginas", "Estado"};
        modelo = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilitar la edición de todas las celdas
            }
        };
        tblTablaLibros.setModel(modelo);

        // Crear un renderer para centrar el texto
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Aplicar el renderer a todas las columnas
        for (int i = 0; i < tblTablaLibros.getColumnCount(); i++) {
            tblTablaLibros.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Cambiar el color de fondo del encabezado
        JTableHeader header = tblTablaLibros.getTableHeader();
        header.setBackground(new Color(0, 102, 153)); // Cambiar color de fondo

        // Cambiar el color del texto del encabezado
        tblTablaLibros.getTableHeader().setForeground(Color.WHITE);

        // Cambiar la fuente del encabezado
        tblTablaLibros.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 18));

        // Color de fondo
        tblTablaLibros.setBackground(Color.WHITE);

        // Estilo de borde
        tblTablaLibros.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tblTablaLibros.setShowGrid(false);

        // Cambiar la fuente y tamaño del texto en las celdas
        tblTablaLibros.setFont(new Font("Monospaced", Font.BOLD, 16));

        tblTablaLibros.setForeground(Color.DARK_GRAY);

        // Cambiar la fuente y tamaño del texto en el encabezado
        tblTablaLibros.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 18));

        // Establecer la altura de las filas
        tblTablaLibros.setRowHeight(40);

        // Establecer anchos de columnas individuales
        tblTablaLibros.getColumnModel().getColumn(0).setPreferredWidth(10);
        tblTablaLibros.getColumnModel().getColumn(1).setPreferredWidth(50);
        tblTablaLibros.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblTablaLibros.getColumnModel().getColumn(3).setPreferredWidth(200);
        tblTablaLibros.getColumnModel().getColumn(4).setPreferredWidth(200);
        tblTablaLibros.getColumnModel().getColumn(5).setPreferredWidth(20);
        tblTablaLibros.getColumnModel().getColumn(6).setPreferredWidth(20);

        // Deshabilitar el reordenamiento de columnas
        tblTablaLibros.getTableHeader().setReorderingAllowed(false);

        // Cambiar el cursor a mano al pasar sobre la tabla
        tblTablaLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                tblTablaLibros.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tblTablaLibros.setCursor(Cursor.getDefaultCursor());
            }
        });

        // Ocultar la primera columna (ID)
        tblTablaLibros.removeColumn(tblTablaLibros.getColumnModel().getColumn(0));

        mostrarDatos();
        // ------------------------------------------------------------------------------------------------------------------------------

        // FORMATEO DE LOS ELEMENTOS DENTRO DEL PANEL AGREGAR --------------------------------------------------------------------------
        //txtISBN.setBorder(new EmptyBorder(0, 10, 0, 10));
        // Crear un borde inferior de 2 píxeles en color negro
        Border border = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE);

        // Aplicar el borde al JTextField
        txtISBN.setBorder(border);
        txtTitulo.setBorder(border);
        txtAutor.setBorder(border);
        txtGenero.setBorder(border);
        txtFechaPublicacion.setBorder(border);
        txtPaginas.setBorder(border);
        txtEditorial.setBorder(border);
        txtSaga.setBorder(border);
        txtSinopsis.setBorder(new EmptyBorder(10, 10, 10, 10));
        cbFormato.setEditable(false);
        cbIdioma.setEditable(false);

        // Formateo para que el jTextArea esbriba en varias líneas
        txtSinopsis.setLineWrap(true); // Permite que el texto se ajuste al ancho
        txtSinopsis.setWrapStyleWord(true); // Salto de línea en los límites de las palabras

        // Validaciones para los jTextField 
        ((PlainDocument) txtISBN.getDocument()).setDocumentFilter(new NumericDocumentFilter(13)); // txtISBN (máximo 13 dígitos)
        ((PlainDocument) txtPaginas.getDocument()).setDocumentFilter(new NumericDocumentFilter(4)); // txtPaginas (máximo 4 dígitos)
        ((PlainDocument) txtFechaPublicacion.getDocument()).setDocumentFilter(new NumericDocumentFilter(4)); // txtFechaPublicacion (máximo 4 dígitos)

        // ------------------------------------------------------------------------------------------------------------------------------
        // FORMATEO DE LOS ELEMENTOS DENTRO DEL PANEL EDITAR ----------------------------------------------------------------------------
        // Aplicar el borde al JTextField
        txtISBNEditar.setBorder(border);
        txtTituloEditar.setBorder(border);
        txtAutorEditar.setBorder(border);
        txtGeneroEditar.setBorder(border);
        txtFechaPublicacionEditar.setBorder(border);
        txtPaginasEditar.setBorder(border);
        txtEditorialEditar.setBorder(border);
        txtSagaEditar.setBorder(border);
        txtFechaInicioEditar.setBorder(border);
        txtFechaFinEditar.setBorder(border);
        txtCalificacionEditar.setBorder(border);
        txtSinopsisEditar.setBorder(new EmptyBorder(10, 10, 10, 10));
        cbFormatoEditar.setEditable(false);
        cbIdiomaEditar.setEditable(false);

        // Formateo para que el jTextArea esbriba en varias líneas
        txtSinopsisEditar.setLineWrap(true); // Permite que el texto se ajuste al ancho
        txtSinopsisEditar.setWrapStyleWord(true); // Salto de línea en los límites de las palabras

        // Validaciones para los jTextField 
        ((PlainDocument) txtISBNEditar.getDocument()).setDocumentFilter(new NumericDocumentFilter(13)); // txtISBN (máximo 13 dígitos)
        ((PlainDocument) txtPaginasEditar.getDocument()).setDocumentFilter(new NumericDocumentFilter(4)); // txtPaginas (máximo 4 dígitos)
        ((PlainDocument) txtFechaPublicacionEditar.getDocument()).setDocumentFilter(new NumericDocumentFilter(4)); // txtFechaPublicacion (máximo 4 dígitos)

        // Aplicar el filtro al txtCalificacionEditar (máximo 2 dígitos)
        ((PlainDocument) txtCalificacionEditar.getDocument()).setDocumentFilter(new NumericDocumentFilter(2));

        // Aplica el filtro a txtFechaInicioEditar
        ((AbstractDocument) txtFechaInicioEditar.getDocument()).setDocumentFilter(new DateNumericDocumentFilter(11));
        ((AbstractDocument) txtFechaFinEditar.getDocument()).setDocumentFilter(new DateNumericDocumentFilter(11));

        // ------------------------------------------------------------------------------------------------------------------------------
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelPrincipal = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        panelBotones = new javax.swing.JPanel();
        btnAgregar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnVerTodos = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        panelTabla = new javax.swing.JPanel();
        panelEditar = new javax.swing.JPanel();
        panelEditarTitulo = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        panelEditar1 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtISBNEditar = new javax.swing.JTextField();
        txtTituloEditar = new javax.swing.JTextField();
        txtAutorEditar = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        panelEditar2 = new javax.swing.JPanel();
        txtGeneroEditar = new javax.swing.JTextField();
        txtEditorialEditar = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        txtSagaEditar = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        txtPaginasEditar = new javax.swing.JTextField();
        txtFechaPublicacionEditar = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        panelEditar3 = new javax.swing.JPanel();
        cbFormatoEditar = new javax.swing.JComboBox<>();
        jLabel38 = new javax.swing.JLabel();
        cbIdiomaEditar = new javax.swing.JComboBox<>();
        jLabel39 = new javax.swing.JLabel();
        txtFechaInicioEditar = new javax.swing.JTextField();
        txtFechaFinEditar = new javax.swing.JTextField();
        txtCalificacionEditar = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        checkTerminadoHoy = new javax.swing.JCheckBox();
        panelEditar4 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        btnCargarPortadaEditar = new javax.swing.JButton();
        lblPortadaEditar = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtSinopsisEditar = new javax.swing.JTextArea();
        panelEditar5 = new javax.swing.JPanel();
        btnLimpiarEditar = new javax.swing.JButton();
        btnGuardarEditar = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        panelAgregar = new javax.swing.JPanel();
        panelAgregarTitulo = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        panelAgregar1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtISBN = new javax.swing.JTextField();
        txtTitulo = new javax.swing.JTextField();
        txtAutor = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        panelAgregar2 = new javax.swing.JPanel();
        txtGenero = new javax.swing.JTextField();
        txtEditorial = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtSaga = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtPaginas = new javax.swing.JTextField();
        txtFechaPublicacion = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        panelAgregar3 = new javax.swing.JPanel();
        cbFormato = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        cbIdioma = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        checkEmpezadoHoy = new javax.swing.JCheckBox();
        panelAgregar4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSinopsis = new javax.swing.JTextArea();
        btnCargarPortada = new javax.swing.JButton();
        lblPortada = new javax.swing.JLabel();
        panelAgregar5 = new javax.swing.JPanel();
        btnGuardarAgregar = new javax.swing.JButton();
        btnLimpiarAgregar = new javax.swing.JButton();
        panelVerTodos = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTablaLibros = new javax.swing.JTable();
        panelPreferidos = new javax.swing.JPanel();
        lblAutorFavorito = new javax.swing.JLabel();
        lblGeneroFavorito = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        panelBotonPDF = new javax.swing.JPanel();
        btnPdf = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelPrincipal.setBackground(new java.awt.Color(51, 51, 51));
        panelPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 102, 153));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/libros96px.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Monospaced", 1, 60)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Mi Biblioteca");

        jLabel3.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("by Antonio Buitrago");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addContainerGap(683, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabel3))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        panelPrincipal.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1300, 100));

        panelBotones.setBackground(new java.awt.Color(204, 204, 204));

        btnAgregar.setBackground(new java.awt.Color(0, 102, 153));
        btnAgregar.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/agregar48px.png"))); // NOI18N
        btnAgregar.setText(" Añadir");
        btnAgregar.setBorder(null);
        btnAgregar.setBorderPainted(false);
        btnAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregar.setFocusPainted(false);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnEditar.setBackground(new java.awt.Color(0, 102, 153));
        btnEditar.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnEditar.setForeground(new java.awt.Color(255, 255, 255));
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/editar48px.png"))); // NOI18N
        btnEditar.setText(" Editar");
        btnEditar.setBorder(null);
        btnEditar.setBorderPainted(false);
        btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditar.setEnabled(false);
        btnEditar.setFocusPainted(false);
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnBorrar.setBackground(new java.awt.Color(0, 102, 153));
        btnBorrar.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnBorrar.setForeground(new java.awt.Color(255, 255, 255));
        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/borrar48px.png"))); // NOI18N
        btnBorrar.setText(" Borrar");
        btnBorrar.setBorder(null);
        btnBorrar.setBorderPainted(false);
        btnBorrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBorrar.setEnabled(false);
        btnBorrar.setFocusPainted(false);
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        btnVerTodos.setBackground(new java.awt.Color(0, 153, 102));
        btnVerTodos.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnVerTodos.setForeground(new java.awt.Color(255, 255, 255));
        btnVerTodos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lista48px.png"))); // NOI18N
        btnVerTodos.setText(" Libros");
        btnVerTodos.setBorder(null);
        btnVerTodos.setBorderPainted(false);
        btnVerTodos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVerTodos.setEnabled(false);
        btnVerTodos.setFocusPainted(false);
        btnVerTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerTodosActionPerformed(evt);
            }
        });

        btnSalir.setBackground(new java.awt.Color(153, 0, 0));
        btnSalir.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/salir48px.png"))); // NOI18N
        btnSalir.setBorder(null);
        btnSalir.setBorderPainted(false);
        btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalir.setFocusPainted(false);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBotonesLayout = new javax.swing.GroupLayout(panelBotones);
        panelBotones.setLayout(panelBotonesLayout);
        panelBotonesLayout.setHorizontalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(btnVerTodos, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        panelBotonesLayout.setVerticalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnVerTodos, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelPrincipal.add(panelBotones, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 1300, -1));

        panelTabla.setBackground(new java.awt.Color(51, 51, 51));
        panelTabla.setLayout(new java.awt.CardLayout());

        panelEditar.setBackground(new java.awt.Color(51, 51, 51));

        panelEditarTitulo.setBackground(new java.awt.Color(0, 153, 102));

        jLabel18.setFont(new java.awt.Font("Monospaced", 1, 48)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Editar");

        javax.swing.GroupLayout panelEditarTituloLayout = new javax.swing.GroupLayout(panelEditarTitulo);
        panelEditarTitulo.setLayout(panelEditarTituloLayout);
        panelEditarTituloLayout.setHorizontalGroup(
            panelEditarTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditarTituloLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel18)
                .addContainerGap(41, Short.MAX_VALUE))
        );
        panelEditarTituloLayout.setVerticalGroup(
            panelEditarTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelEditar1.setBackground(new java.awt.Color(51, 51, 51));

        jLabel20.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("ISBN");

        txtISBNEditar.setBackground(new java.awt.Color(51, 51, 51));
        txtISBNEditar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtISBNEditar.setBorder(null);
        txtISBNEditar.setEnabled(false);
        txtISBNEditar.setPreferredSize(new java.awt.Dimension(1, 35));

        txtTituloEditar.setBackground(new java.awt.Color(51, 51, 51));
        txtTituloEditar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtTituloEditar.setBorder(null);
        txtTituloEditar.setPreferredSize(new java.awt.Dimension(1, 35));

        txtAutorEditar.setBackground(new java.awt.Color(51, 51, 51));
        txtAutorEditar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtAutorEditar.setBorder(null);
        txtAutorEditar.setPreferredSize(new java.awt.Dimension(1, 35));

        jLabel21.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Título del libro");

        jLabel22.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Autor del libro");

        javax.swing.GroupLayout panelEditar1Layout = new javax.swing.GroupLayout(panelEditar1);
        panelEditar1.setLayout(panelEditar1Layout);
        panelEditar1Layout.setHorizontalGroup(
            panelEditar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditar1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelEditar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtISBNEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addGap(18, 18, 18)
                .addGroup(panelEditar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTituloEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelEditar1Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(0, 231, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(panelEditar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(txtAutorEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        panelEditar1Layout.setVerticalGroup(
            panelEditar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditar1Layout.createSequentialGroup()
                .addGroup(panelEditar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelEditar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtISBNEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTituloEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAutorEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        panelEditar2.setBackground(new java.awt.Color(51, 51, 51));

        txtGeneroEditar.setBackground(new java.awt.Color(51, 51, 51));
        txtGeneroEditar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtGeneroEditar.setBorder(null);
        txtGeneroEditar.setPreferredSize(new java.awt.Dimension(1, 35));

        txtEditorialEditar.setBackground(new java.awt.Color(51, 51, 51));
        txtEditorialEditar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtEditorialEditar.setBorder(null);
        txtEditorialEditar.setPreferredSize(new java.awt.Dimension(1, 35));

        jLabel33.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Género");

        txtSagaEditar.setBackground(new java.awt.Color(51, 51, 51));
        txtSagaEditar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtSagaEditar.setBorder(null);
        txtSagaEditar.setPreferredSize(new java.awt.Dimension(1, 35));

        jLabel34.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Editorial");

        jLabel35.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Saga");

        txtPaginasEditar.setBackground(new java.awt.Color(51, 51, 51));
        txtPaginasEditar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtPaginasEditar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPaginasEditar.setBorder(null);
        txtPaginasEditar.setPreferredSize(new java.awt.Dimension(1, 35));

        txtFechaPublicacionEditar.setBackground(new java.awt.Color(51, 51, 51));
        txtFechaPublicacionEditar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtFechaPublicacionEditar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFechaPublicacionEditar.setBorder(null);
        txtFechaPublicacionEditar.setPreferredSize(new java.awt.Dimension(1, 35));

        jLabel36.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Páginas");

        jLabel37.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Año Publicación");

        javax.swing.GroupLayout panelEditar2Layout = new javax.swing.GroupLayout(panelEditar2);
        panelEditar2.setLayout(panelEditar2Layout);
        panelEditar2Layout.setHorizontalGroup(
            panelEditar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditar2Layout.createSequentialGroup()
                .addGroup(panelEditar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGeneroEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addGap(18, 18, 18)
                .addGroup(panelEditar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEditorialEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addGap(18, 18, 18)
                .addGroup(panelEditar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSagaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35))
                .addGroup(panelEditar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEditar2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtPaginasEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEditar2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)))
                .addGroup(panelEditar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEditar2Layout.createSequentialGroup()
                        .addComponent(txtFechaPublicacionEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)))
                .addContainerGap())
        );
        panelEditar2Layout.setVerticalGroup(
            panelEditar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEditar2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelEditar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(jLabel34)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36)
                    .addComponent(jLabel37))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelEditar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGeneroEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEditorialEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSagaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPaginasEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFechaPublicacionEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        panelEditar3.setBackground(new java.awt.Color(51, 51, 51));

        cbFormatoEditar.setEditable(true);
        cbFormatoEditar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        cbFormatoEditar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Papel", "eBook" }));
        cbFormatoEditar.setBorder(null);
        cbFormatoEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cbFormatoEditar.setPreferredSize(new java.awt.Dimension(127, 36));

        jLabel38.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Formato");

        cbIdiomaEditar.setEditable(true);
        cbIdiomaEditar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        cbIdiomaEditar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Español", "Inglés" }));
        cbIdiomaEditar.setBorder(null);
        cbIdiomaEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cbIdiomaEditar.setPreferredSize(new java.awt.Dimension(127, 36));

        jLabel39.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Idioma");

        txtFechaInicioEditar.setBackground(new java.awt.Color(51, 51, 51));
        txtFechaInicioEditar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtFechaInicioEditar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFechaInicioEditar.setBorder(null);
        txtFechaInicioEditar.setPreferredSize(new java.awt.Dimension(1, 35));

        txtFechaFinEditar.setBackground(new java.awt.Color(51, 51, 51));
        txtFechaFinEditar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtFechaFinEditar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFechaFinEditar.setBorder(null);
        txtFechaFinEditar.setPreferredSize(new java.awt.Dimension(1, 35));

        txtCalificacionEditar.setBackground(new java.awt.Color(51, 51, 51));
        txtCalificacionEditar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtCalificacionEditar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCalificacionEditar.setBorder(null);
        txtCalificacionEditar.setPreferredSize(new java.awt.Dimension(1, 35));

        jLabel40.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Fecha Inicio (*)");

        jLabel41.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Fecha Fin (*)");

        jLabel42.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Nota");

        checkTerminadoHoy.setBackground(new java.awt.Color(51, 51, 51));
        checkTerminadoHoy.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        checkTerminadoHoy.setForeground(new java.awt.Color(255, 255, 255));
        checkTerminadoHoy.setText(" Terminado hoy");
        checkTerminadoHoy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        checkTerminadoHoy.setEnabled(false);

        javax.swing.GroupLayout panelEditar3Layout = new javax.swing.GroupLayout(panelEditar3);
        panelEditar3.setLayout(panelEditar3Layout);
        panelEditar3Layout.setHorizontalGroup(
            panelEditar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditar3Layout.createSequentialGroup()
                .addGroup(panelEditar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbFormatoEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelEditar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbIdiomaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39))
                .addGap(18, 18, 18)
                .addGroup(panelEditar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFechaInicioEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40))
                .addGap(18, 18, 18)
                .addGroup(panelEditar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEditar3Layout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addGap(70, 70, 70)
                        .addComponent(jLabel42)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelEditar3Layout.createSequentialGroup()
                        .addComponent(txtFechaFinEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtCalificacionEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(checkTerminadoHoy)
                        .addContainerGap())))
        );
        panelEditar3Layout.setVerticalGroup(
            panelEditar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditar3Layout.createSequentialGroup()
                .addGroup(panelEditar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(jLabel39)
                    .addComponent(jLabel40)
                    .addComponent(jLabel41)
                    .addComponent(jLabel42))
                .addGap(6, 6, 6)
                .addGroup(panelEditar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbFormatoEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbIdiomaEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtFechaInicioEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFechaFinEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCalificacionEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkTerminadoHoy))
                .addContainerGap())
        );

        panelEditar4.setBackground(new java.awt.Color(51, 51, 51));

        jLabel43.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Sinopsis");

        btnCargarPortadaEditar.setBackground(new java.awt.Color(0, 102, 153));
        btnCargarPortadaEditar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        btnCargarPortadaEditar.setForeground(new java.awt.Color(255, 255, 255));
        btnCargarPortadaEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cargar32px.png"))); // NOI18N
        btnCargarPortadaEditar.setText(" Cargar");
        btnCargarPortadaEditar.setBorder(null);
        btnCargarPortadaEditar.setBorderPainted(false);
        btnCargarPortadaEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCargarPortadaEditar.setFocusPainted(false);
        btnCargarPortadaEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarPortadaEditarActionPerformed(evt);
            }
        });

        lblPortadaEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/portadaLibro.png"))); // NOI18N
        lblPortadaEditar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtSinopsisEditar.setBackground(new java.awt.Color(51, 51, 51));
        txtSinopsisEditar.setColumns(20);
        txtSinopsisEditar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtSinopsisEditar.setRows(5);
        txtSinopsisEditar.setBorder(null);
        jScrollPane3.setViewportView(txtSinopsisEditar);

        javax.swing.GroupLayout panelEditar4Layout = new javax.swing.GroupLayout(panelEditar4);
        panelEditar4.setLayout(panelEditar4Layout);
        panelEditar4Layout.setHorizontalGroup(
            panelEditar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditar4Layout.createSequentialGroup()
                .addGroup(panelEditar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPortadaEditar)
                    .addComponent(btnCargarPortadaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelEditar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        panelEditar4Layout.setVerticalGroup(
            panelEditar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditar4Layout.createSequentialGroup()
                .addGroup(panelEditar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEditar4Layout.createSequentialGroup()
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3))
                    .addGroup(panelEditar4Layout.createSequentialGroup()
                        .addComponent(lblPortadaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCargarPortadaEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        panelEditar5.setBackground(new java.awt.Color(51, 51, 51));

        btnLimpiarEditar.setBackground(new java.awt.Color(0, 102, 153));
        btnLimpiarEditar.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnLimpiarEditar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/limpiar.png"))); // NOI18N
        btnLimpiarEditar.setText(" Limpiar");
        btnLimpiarEditar.setBorder(null);
        btnLimpiarEditar.setBorderPainted(false);
        btnLimpiarEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiarEditar.setFocusPainted(false);
        btnLimpiarEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarEditarActionPerformed(evt);
            }
        });

        btnGuardarEditar.setBackground(new java.awt.Color(0, 153, 102));
        btnGuardarEditar.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnGuardarEditar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardarEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/guardar.png"))); // NOI18N
        btnGuardarEditar.setText(" Guardar");
        btnGuardarEditar.setBorder(null);
        btnGuardarEditar.setBorderPainted(false);
        btnGuardarEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarEditar.setFocusPainted(false);
        btnGuardarEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarEditarActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel23.setText("(*) Fechas en formato 01/01/2000");

        javax.swing.GroupLayout panelEditar5Layout = new javax.swing.GroupLayout(panelEditar5);
        panelEditar5.setLayout(panelEditar5Layout);
        panelEditar5Layout.setHorizontalGroup(
            panelEditar5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditar5Layout.createSequentialGroup()
                .addGroup(panelEditar5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLimpiarEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelEditar5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel23)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnGuardarEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelEditar5Layout.setVerticalGroup(
            panelEditar5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEditar5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addComponent(btnGuardarEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLimpiarEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelEditarLayout = new javax.swing.GroupLayout(panelEditar);
        panelEditar.setLayout(panelEditarLayout);
        panelEditarLayout.setHorizontalGroup(
            panelEditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEditarLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(panelEditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelEditarLayout.createSequentialGroup()
                        .addComponent(panelEditarTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addComponent(panelEditar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelEditar3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelEditarLayout.createSequentialGroup()
                        .addComponent(panelEditar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panelEditar5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(panelEditar2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        panelEditarLayout.setVerticalGroup(
            panelEditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditarLayout.createSequentialGroup()
                .addGroup(panelEditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelEditarTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelEditar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelEditar2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelEditar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelEditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEditarLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(panelEditar5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelEditarLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelEditar4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        panelTabla.add(panelEditar, "card4");

        panelAgregar.setBackground(new java.awt.Color(51, 51, 51));

        panelAgregarTitulo.setBackground(new java.awt.Color(0, 102, 153));

        jLabel5.setFont(new java.awt.Font("Monospaced", 1, 48)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Agregar");

        javax.swing.GroupLayout panelAgregarTituloLayout = new javax.swing.GroupLayout(panelAgregarTitulo);
        panelAgregarTitulo.setLayout(panelAgregarTituloLayout);
        panelAgregarTituloLayout.setHorizontalGroup(
            panelAgregarTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregarTituloLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel5)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        panelAgregarTituloLayout.setVerticalGroup(
            panelAgregarTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAgregarTituloLayout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelAgregar1.setBackground(new java.awt.Color(51, 51, 51));

        jLabel7.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("ISBN");

        txtISBN.setBackground(new java.awt.Color(51, 51, 51));
        txtISBN.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtISBN.setBorder(null);
        txtISBN.setPreferredSize(new java.awt.Dimension(1, 35));

        txtTitulo.setBackground(new java.awt.Color(51, 51, 51));
        txtTitulo.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtTitulo.setBorder(null);
        txtTitulo.setPreferredSize(new java.awt.Dimension(1, 35));

        txtAutor.setBackground(new java.awt.Color(51, 51, 51));
        txtAutor.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtAutor.setBorder(null);
        txtAutor.setPreferredSize(new java.awt.Dimension(1, 35));

        jLabel8.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Título del libro");

        jLabel9.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Autor del libro");

        javax.swing.GroupLayout panelAgregar1Layout = new javax.swing.GroupLayout(panelAgregar1);
        panelAgregar1.setLayout(panelAgregar1Layout);
        panelAgregar1Layout.setHorizontalGroup(
            panelAgregar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregar1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelAgregar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(panelAgregar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelAgregar1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 231, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(panelAgregar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(txtAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        panelAgregar1Layout.setVerticalGroup(
            panelAgregar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregar1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAgregar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAgregar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelAgregar2.setBackground(new java.awt.Color(51, 51, 51));

        txtGenero.setBackground(new java.awt.Color(51, 51, 51));
        txtGenero.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtGenero.setBorder(null);
        txtGenero.setPreferredSize(new java.awt.Dimension(1, 35));

        txtEditorial.setBackground(new java.awt.Color(51, 51, 51));
        txtEditorial.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtEditorial.setBorder(null);
        txtEditorial.setPreferredSize(new java.awt.Dimension(1, 35));

        jLabel10.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Género");

        txtSaga.setBackground(new java.awt.Color(51, 51, 51));
        txtSaga.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtSaga.setBorder(null);
        txtSaga.setPreferredSize(new java.awt.Dimension(1, 35));

        jLabel11.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Editorial");

        jLabel12.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Saga");

        txtPaginas.setBackground(new java.awt.Color(51, 51, 51));
        txtPaginas.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtPaginas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPaginas.setBorder(null);
        txtPaginas.setPreferredSize(new java.awt.Dimension(1, 35));

        txtFechaPublicacion.setBackground(new java.awt.Color(51, 51, 51));
        txtFechaPublicacion.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtFechaPublicacion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFechaPublicacion.setBorder(null);
        txtFechaPublicacion.setPreferredSize(new java.awt.Dimension(1, 35));

        jLabel14.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Páginas");

        jLabel13.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Año Publicación");

        javax.swing.GroupLayout panelAgregar2Layout = new javax.swing.GroupLayout(panelAgregar2);
        panelAgregar2.setLayout(panelAgregar2Layout);
        panelAgregar2Layout.setHorizontalGroup(
            panelAgregar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregar2Layout.createSequentialGroup()
                .addGroup(panelAgregar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGenero, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(panelAgregar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(panelAgregar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSaga, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(panelAgregar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPaginas, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelAgregar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFechaPublicacion, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelAgregar2Layout.setVerticalGroup(
            panelAgregar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAgregar2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelAgregar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAgregar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSaga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPaginas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFechaPublicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        panelAgregar3.setBackground(new java.awt.Color(51, 51, 51));

        cbFormato.setEditable(true);
        cbFormato.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        cbFormato.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Papel", "eBook" }));
        cbFormato.setBorder(null);
        cbFormato.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cbFormato.setPreferredSize(new java.awt.Dimension(127, 36));

        jLabel15.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Formato");

        cbIdioma.setEditable(true);
        cbIdioma.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        cbIdioma.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Español", "Inglés" }));
        cbIdioma.setBorder(null);
        cbIdioma.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cbIdioma.setPreferredSize(new java.awt.Dimension(127, 36));

        jLabel16.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Idioma");

        checkEmpezadoHoy.setBackground(new java.awt.Color(51, 51, 51));
        checkEmpezadoHoy.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        checkEmpezadoHoy.setForeground(new java.awt.Color(255, 255, 255));
        checkEmpezadoHoy.setText(" Empezado hoy");
        checkEmpezadoHoy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout panelAgregar3Layout = new javax.swing.GroupLayout(panelAgregar3);
        panelAgregar3.setLayout(panelAgregar3Layout);
        panelAgregar3Layout.setHorizontalGroup(
            panelAgregar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregar3Layout.createSequentialGroup()
                .addGroup(panelAgregar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbFormato, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelAgregar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAgregar3Layout.createSequentialGroup()
                        .addComponent(cbIdioma, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(checkEmpezadoHoy))
                    .addComponent(jLabel16))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelAgregar3Layout.setVerticalGroup(
            panelAgregar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregar3Layout.createSequentialGroup()
                .addGroup(panelAgregar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addGap(6, 6, 6)
                .addGroup(panelAgregar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbFormato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbIdioma, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(checkEmpezadoHoy))
                .addContainerGap())
        );

        panelAgregar4.setBackground(new java.awt.Color(51, 51, 51));

        jLabel17.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Sinopsis");

        txtSinopsis.setBackground(new java.awt.Color(51, 51, 51));
        txtSinopsis.setColumns(20);
        txtSinopsis.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtSinopsis.setRows(5);
        txtSinopsis.setBorder(null);
        jScrollPane2.setViewportView(txtSinopsis);

        btnCargarPortada.setBackground(new java.awt.Color(0, 102, 153));
        btnCargarPortada.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        btnCargarPortada.setForeground(new java.awt.Color(255, 255, 255));
        btnCargarPortada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cargar32px.png"))); // NOI18N
        btnCargarPortada.setText(" Cargar");
        btnCargarPortada.setBorder(null);
        btnCargarPortada.setBorderPainted(false);
        btnCargarPortada.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCargarPortada.setFocusPainted(false);
        btnCargarPortada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarPortadaActionPerformed(evt);
            }
        });

        lblPortada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/portadaLibro.png"))); // NOI18N
        lblPortada.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout panelAgregar4Layout = new javax.swing.GroupLayout(panelAgregar4);
        panelAgregar4.setLayout(panelAgregar4Layout);
        panelAgregar4Layout.setHorizontalGroup(
            panelAgregar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAgregar4Layout.createSequentialGroup()
                .addGroup(panelAgregar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAgregar4Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(488, 627, Short.MAX_VALUE))
                    .addGroup(panelAgregar4Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addGap(18, 18, 18)))
                .addGroup(panelAgregar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCargarPortada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPortada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27))
        );
        panelAgregar4Layout.setVerticalGroup(
            panelAgregar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregar4Layout.createSequentialGroup()
                .addGroup(panelAgregar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAgregar4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2))
                    .addGroup(panelAgregar4Layout.createSequentialGroup()
                        .addComponent(lblPortada, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addComponent(btnCargarPortada, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        panelAgregar5.setBackground(new java.awt.Color(51, 51, 51));

        btnGuardarAgregar.setBackground(new java.awt.Color(0, 153, 102));
        btnGuardarAgregar.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnGuardarAgregar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardarAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/guardar.png"))); // NOI18N
        btnGuardarAgregar.setText(" Guardar");
        btnGuardarAgregar.setBorder(null);
        btnGuardarAgregar.setBorderPainted(false);
        btnGuardarAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarAgregar.setEnabled(false);
        btnGuardarAgregar.setFocusPainted(false);
        btnGuardarAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarAgregarActionPerformed(evt);
            }
        });

        btnLimpiarAgregar.setBackground(new java.awt.Color(0, 102, 153));
        btnLimpiarAgregar.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnLimpiarAgregar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/limpiar.png"))); // NOI18N
        btnLimpiarAgregar.setText(" Limpiar");
        btnLimpiarAgregar.setBorder(null);
        btnLimpiarAgregar.setBorderPainted(false);
        btnLimpiarAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiarAgregar.setFocusPainted(false);
        btnLimpiarAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarAgregarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAgregar5Layout = new javax.swing.GroupLayout(panelAgregar5);
        panelAgregar5.setLayout(panelAgregar5Layout);
        panelAgregar5Layout.setHorizontalGroup(
            panelAgregar5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregar5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnLimpiarAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(btnGuardarAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelAgregar5Layout.setVerticalGroup(
            panelAgregar5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAgregar5Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(btnGuardarAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLimpiarAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelAgregarLayout = new javax.swing.GroupLayout(panelAgregar);
        panelAgregar.setLayout(panelAgregarLayout);
        panelAgregarLayout.setHorizontalGroup(
            panelAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregarLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panelAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelAgregarLayout.createSequentialGroup()
                        .addComponent(panelAgregar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(panelAgregar5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(panelAgregar2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(panelAgregarLayout.createSequentialGroup()
                            .addComponent(panelAgregarTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(35, 35, 35)
                            .addComponent(panelAgregar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(panelAgregar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        panelAgregarLayout.setVerticalGroup(
            panelAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelAgregarTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelAgregar1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelAgregar2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAgregar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelAgregar5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelAgregar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelTabla.add(panelAgregar, "card2");

        panelVerTodos.setBackground(new java.awt.Color(51, 51, 51));

        jPanel2.setBackground(new java.awt.Color(0, 102, 153));

        jLabel4.setFont(new java.awt.Font("Monospaced", 1, 60)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Listado de Libros");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(341, 341, 341)
                .addComponent(jLabel4)
                .addContainerGap(318, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        tblTablaLibros.setAutoCreateRowSorter(true);
        tblTablaLibros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTablaLibros.setSelectionBackground(new java.awt.Color(0, 102, 153));
        tblTablaLibros.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tblTablaLibros.setShowGrid(true);
        tblTablaLibros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTablaLibrosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblTablaLibros);

        panelPreferidos.setBackground(new java.awt.Color(0, 102, 153));

        lblAutorFavorito.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        lblAutorFavorito.setText("Aún no tienes un autor favorito.");

        lblGeneroFavorito.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        lblGeneroFavorito.setText("Aún no tienes un género favorito.");

        jLabel24.setFont(new java.awt.Font("Bebas Neue", 1, 32)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/favoritos32px.png"))); // NOI18N
        jLabel24.setText(" tus favoritos");

        javax.swing.GroupLayout panelPreferidosLayout = new javax.swing.GroupLayout(panelPreferidos);
        panelPreferidos.setLayout(panelPreferidosLayout);
        panelPreferidosLayout.setHorizontalGroup(
            panelPreferidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPreferidosLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelPreferidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGeneroFavorito, javax.swing.GroupLayout.PREFERRED_SIZE, 931, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAutorFavorito, javax.swing.GroupLayout.PREFERRED_SIZE, 931, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        panelPreferidosLayout.setVerticalGroup(
            panelPreferidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPreferidosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblAutorFavorito)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblGeneroFavorito)
                .addGap(18, 18, 18))
        );

        panelBotonPDF.setBackground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout panelBotonPDFLayout = new javax.swing.GroupLayout(panelBotonPDF);
        panelBotonPDF.setLayout(panelBotonPDFLayout);
        panelBotonPDFLayout.setHorizontalGroup(
            panelBotonPDFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 288, Short.MAX_VALUE)
        );
        panelBotonPDFLayout.setVerticalGroup(
            panelBotonPDFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );

        btnPdf.setBackground(new java.awt.Color(153, 0, 0));
        btnPdf.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnPdf.setForeground(new java.awt.Color(255, 255, 255));
        btnPdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pdf32px.png"))); // NOI18N
        btnPdf.setText(" Crear PDF");
        btnPdf.setBorder(null);
        btnPdf.setBorderPainted(false);
        btnPdf.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPdf.setFocusPainted(false);
        btnPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPdfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelVerTodosLayout = new javax.swing.GroupLayout(panelVerTodos);
        panelVerTodos.setLayout(panelVerTodosLayout);
        panelVerTodosLayout.setHorizontalGroup(
            panelVerTodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVerTodosLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(panelVerTodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelVerTodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(panelVerTodosLayout.createSequentialGroup()
                            .addComponent(panelPreferidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelVerTodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(panelBotonPDF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnPdf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        panelVerTodosLayout.setVerticalGroup(
            panelVerTodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVerTodosLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelVerTodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelVerTodosLayout.createSequentialGroup()
                        .addGap(128, 128, 128)
                        .addComponent(panelBotonPDF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(14, 14, 14))
                    .addGroup(panelVerTodosLayout.createSequentialGroup()
                        .addGroup(panelVerTodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(panelPreferidos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPdf, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(12, Short.MAX_VALUE))))
        );

        panelTabla.add(panelVerTodos, "card3");

        panelPrincipal.add(panelTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 1300, 570));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 768, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // EVENTO CLICK DEL BOTÓN AÑADIR ----------------------------------------------------------------------------------------------------
    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        panelAgregar.setVisible(true);
        panelVerTodos.setVisible(false);
        panelEditar.setVisible(false);

        btnVerTodos.setEnabled(true);
        btnAgregar.setEnabled(false);
        btnBorrar.setEnabled(false);
    }//GEN-LAST:event_btnAgregarActionPerformed
    // ----------------------------------------------------------------------------------------------------------------------------------

    // EVENTO CLICK DEL BOTÓN EDITAR ----------------------------------------------------------------------------------------------------
    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        panelVerTodos.setVisible(false);
        panelEditar.setVisible(true);
        panelAgregar.setVisible(false);
        btnVerTodos.setEnabled(true);
        btnAgregar.setEnabled(false);
        btnBorrar.setEnabled(false);
        btnEditar.setEnabled(false);

        Conexion objConexion = new Conexion();

        int filaSeleccionada = tblTablaLibros.getSelectedRow(); // Obtener la fila seleccionada
        if (filaSeleccionada != -1) { // Verificar que hay una fila seleccionada
            // Obtener el valor de ISBN de la primera columna
            String idLibro = tblTablaLibros.getModel().getValueAt(filaSeleccionada, 0).toString();

            // Crear la sentencia SELECT utilizando el valor de ISBN
            String strSentenciaSelect = String.format("SELECT * FROM libros WHERE id_libro = %s", idLibro);

            // Ejecutar la consulta.
            ResultSet resultadoConsulta = objConexion.consultarRegistros(strSentenciaSelect);

            try {
                if (resultadoConsulta.next()) {
                    // Crear un objeto Libro y rellenarlo con los datos del ResultSet
                    Libro oLibro = new Libro();
                    oLibro.setId_libro(resultadoConsulta.getInt("id_libro"));
                    oLibro.setIsbn(resultadoConsulta.getString("isbn"));
                    oLibro.setTitulo(resultadoConsulta.getString("titulo"));
                    oLibro.setAutor(resultadoConsulta.getString("autor"));
                    oLibro.setGenero(resultadoConsulta.getString("genero"));

                    // Fecha Inicio --------------------------------------------
                    String fechaInicioStr = resultadoConsulta.getString("fecha_inicio");
                    java.util.Date fechaInicio = null; // Inicializa la variable

                    // Guardar los filtros actuales
                    ((AbstractDocument) txtFechaInicioEditar.getDocument()).setDocumentFilter(new DateNumericDocumentFilter(11));
                    // Desactivar temporalmente los filtros
                    ((AbstractDocument) txtFechaInicioEditar.getDocument()).setDocumentFilter(null);

                    // Verifica que fechaInicioStr no sea null y no esté vacío
                    if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            fechaInicio = formatoFecha.parse(fechaInicioStr);
                            oLibro.setFecha_inicio(fechaInicio);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        oLibro.setFecha_inicio(null); // Establece a null si la cadena está vacía o es null
                    }

                    // Muestra la fecha en el JTextField en el formato dd/MM/yyyy
                    if (fechaInicio != null) {
                        SimpleDateFormat formatoFechaMostrar = new SimpleDateFormat("dd/MM/yyyy");
                        String fechaInicioEditarStr = formatoFechaMostrar.format(fechaInicio);
                        txtFechaInicioEditar.setText(fechaInicioEditarStr);
                    } else {
                        txtFechaInicioEditar.setText(""); // Asegúrate de que el JTextField esté vacío si no hay fecha
                    }
                    ((AbstractDocument) txtFechaInicioEditar.getDocument()).setDocumentFilter(new DateNumericDocumentFilter(11));
                    // ---------------------------------------------------------

                    // Fecha Final --------------------------------------------
                    String fechaFinalStr = resultadoConsulta.getString("fecha_fin");
                    java.util.Date fechaFinal = null; // Inicializa la variable
                    // Guardar los filtros actuales
                    ((AbstractDocument) txtFechaFinEditar.getDocument()).setDocumentFilter(new DateNumericDocumentFilter(11));
                    // Desactivar temporalmente los filtros
                    ((AbstractDocument) txtFechaFinEditar.getDocument()).setDocumentFilter(null);

                    // Verifica que fechaFinalStr no sea null y no esté vacío
                    if (fechaFinalStr != null && !fechaFinalStr.isEmpty()) {
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            fechaFinal = formatoFecha.parse(fechaFinalStr);
                            oLibro.setFecha_fin(fechaFinal);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        oLibro.setFecha_fin(null); // Establece a null si la cadena está vacía o es null
                    }

                    // Muestra la fecha en el JTextField en el formato dd/MM/yyyy
                    if (fechaFinal != null) { // Asegúrate de usar fechaFinal aquí
                        SimpleDateFormat formatoFechaMostrar = new SimpleDateFormat("dd/MM/yyyy");
                        String fechaFinEditarStr = formatoFechaMostrar.format(fechaFinal);
                        txtFechaFinEditar.setText(fechaFinEditarStr); // Usa txtFechaFinEditar
                    } else {
                        txtFechaFinEditar.setText(""); // Asegúrate de que el JTextField esté vacío si no hay fecha
                    }
                    ((AbstractDocument) txtFechaFinEditar.getDocument()).setDocumentFilter(new DateNumericDocumentFilter(11));
                    // ---------------------------------------------------------

                    oLibro.setEstado_lectura(resultadoConsulta.getString("estado_lectura"));
                    oLibro.setCalificacion(resultadoConsulta.getInt("calificacion"));
                    oLibro.setSinopsis(resultadoConsulta.getString("sinopsis"));
                    oLibro.setNumero_paginas(resultadoConsulta.getInt("numero_paginas"));
                    oLibro.setFormato(resultadoConsulta.getString("formato"));
                    oLibro.setIdioma(resultadoConsulta.getString("idioma"));
                    oLibro.setFecha_publicacion(resultadoConsulta.getInt("fecha_publicacion"));
                    oLibro.setEditorial(resultadoConsulta.getString("editorial"));
                    oLibro.setSaga(resultadoConsulta.getString("saga"));
                    oLibro.setPortada(resultadoConsulta.getString("portada"));

                    txtISBNEditar.setText(oLibro.getIsbn());
                    txtTituloEditar.setText(oLibro.getTitulo());
                    txtAutorEditar.setText(oLibro.getAutor());
                    txtGeneroEditar.setText(oLibro.getGenero());
                    txtEditorialEditar.setText(oLibro.getEditorial());
                    if (oLibro.getSaga() != null) {
                        txtSagaEditar.setText(oLibro.getSaga());
                    } else {
                        txtSagaEditar.setText("");
                    }
                    txtFechaPublicacionEditar.setText(String.valueOf(oLibro.getFecha_publicacion()));
                    txtPaginasEditar.setText(String.valueOf(oLibro.getNumero_paginas()));
                    cbFormatoEditar.setSelectedItem(oLibro.getFormato());
                    cbIdiomaEditar.setSelectedItem(oLibro.getIdioma());
                    if (oLibro.getCalificacion() != 0) {
                        txtCalificacionEditar.setText(String.valueOf(oLibro.getCalificacion()));
                    } else {
                        txtCalificacionEditar.setText("");
                    }
                    txtSinopsisEditar.setText(String.valueOf(oLibro.getSinopsis()));

                    // Portada del libro ---------------------------------------
                    String nombrePortada = oLibro.getPortada(); // Debes asegurarte de que no sea null

                    if (nombrePortada != null && !nombrePortada.isEmpty()) {
                        // Construye la ruta completa de la imagen
                        // Obtener la ruta del directorio del usuario
                        String userHome = System.getProperty("user.home");

                        // Definir la carpeta destino en la ruta del sistema del usuario
                        String carpetaDestino = userHome + "/Mi Biblioteca/portadas/";

                        // Construir la ruta completa de la imagen
                        String imageUrl = carpetaDestino + nombrePortada;

                        if (imageUrl != null) {
                            // Carga la imagen desde la URL
                            ImageIcon originalIcon = new ImageIcon(imageUrl);
                            Image originalImage = originalIcon.getImage();

                            // Escala la imagen al tamaño del JLabel
                            Image scaledImage = originalImage.getScaledInstance(176, 261, Image.SCALE_DEFAULT);

                            // Establece la imagen escalada como ícono en el JLabel
                            lblPortadaEditar.setIcon(new ImageIcon(scaledImage));
                        } else {
                            // Maneja el caso donde la imagen no se encuentra
                            mostrarImagenPorDefecto(lblPortadaEditar); // Llamar al método que muestra la imagen por defecto
                        }
                    } else {
                        // Maneja el caso donde la imagen no se encuentra
                        mostrarImagenPorDefecto(lblPortadaEditar); // Llamar al método que muestra la imagen por defecto
                    }
                    // ---------------------------------------------------------
                    // Agregar el ItemListener al JCheckBox
                    checkTerminadoHoy.addItemListener(new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent e) {
                            if (checkTerminadoHoy.isSelected()) {

                                // Guardar los filtros actuales
                                ((AbstractDocument) txtFechaFinEditar.getDocument()).setDocumentFilter(new DateNumericDocumentFilter(11));
                                // Desactivar temporalmente los filtros
                                ((AbstractDocument) txtFechaFinEditar.getDocument()).setDocumentFilter(null);

                                // Obtener la fecha actual
                                String fechaHoy = new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
                                txtFechaFinEditar.setText(fechaHoy);

                            } else {
                                // Vaciar el JTextField
                                txtFechaFinEditar.setText("");
                            }
                            // Restaurar los filtros originales
                            ((AbstractDocument) txtFechaFinEditar.getDocument()).setDocumentFilter(new DateNumericDocumentFilter(11));
                        }
                    });

                    // Agregar DocumentListener al JTextField de fecha de inicio
                    txtFechaInicioEditar.getDocument().addDocumentListener(new DocumentListener() {

                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            updateFields();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            updateFields();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            updateFields();
                        }

                        // Método para habilitar o deshabilitar los campos
                        private void updateFields() {
                            boolean hasDate = !txtFechaInicioEditar.getText().trim().isEmpty();
                            txtFechaFinEditar.setEnabled(hasDate);
                            checkTerminadoHoy.setEnabled(hasDate);

                            // Si no hay fecha en txtFechaInicioEditar, limpiar txtFechaFinEditar y checkbox
                            if (!hasDate) {
                                txtFechaFinEditar.setText("");
                                checkTerminadoHoy.setSelected(false); // Desmarcar el checkbox
                            }

                            // Verificar y habilitar el botón
                            verificarCamposYActivarBoton();
                        }

                    });

                    // Agregar un ComponentListener al panel de edición
                    panelEditar.addComponentListener(new ComponentAdapter() {
                        @Override
                        public void componentShown(ComponentEvent e) {
                            // Verificar si txtFechaInicioEditar tiene un valor y habilitar el checkbox si no está vacío
                            if (!txtFechaInicioEditar.getText().trim().isEmpty()) {
                                checkTerminadoHoy.setEnabled(true); // Habilita el checkbox
                            } else {
                                checkTerminadoHoy.setEnabled(false); // Deshabilita el checkbox
                            }
                        }
                    });

                    // Document Listener para el campo Calificación, pero que nunca se mayor que 10.
                    txtCalificacionEditar.getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            validarCalificacion();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            validarCalificacion();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            validarCalificacion();
                        }

                        // Método para validar la calificación
                        private void validarCalificacion() {
                            // Obtener el texto actual del JTextField
                            String texto = txtCalificacionEditar.getText().trim();

                            // Verificar si el texto no está vacío
                            if (!texto.isEmpty()) {
                                try {
                                    // Parsear el texto a número entero
                                    int calificacion = Integer.parseInt(texto);

                                    // Si la calificación es mayor a 10, establecerla como 10
                                    if (calificacion > 10) {
                                        SwingUtilities.invokeLater(() -> txtCalificacionEditar.setText("10"));
                                    }
                                } catch (NumberFormatException ex) {
                                    // Si ocurre una excepción (por ejemplo, el texto no es un número válido), se puede manejar aquí
                                    // Puedes agregar un mensaje de error o simplemente hacer nada
                                }
                            }
                        }
                    });

                    resultadoConsulta.close();
                    objConexion.closeConnection();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_btnEditarActionPerformed

    // EVENTO CLICK DEL BOTÓN BORRAR ----------------------------------------------------------------------------------------------------
    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        Conexion objConexion = new Conexion();

        int filaSeleccionada = tblTablaLibros.getSelectedRow(); // Obtener la fila seleccionada

        if (filaSeleccionada != -1) { // Verificar que hay una fila seleccionada
            // Obtener el valor de ISBN de la primera columna
            String idLibro = tblTablaLibros.getModel().getValueAt(filaSeleccionada, 0).toString();

            // Crear la sentencia DELETE utilizando el valor de ISBN
            String strSentenciaDelete = String.format("DELETE FROM libros WHERE id_libro = %s", idLibro);

            // Pasar la sentencia SQL a la ventana de confirmación
            VentanaPreguntaBorrar borrado = new VentanaPreguntaBorrar(strSentenciaDelete);
            borrado.setVisible(true);
            // Calcula la posición para centrarla dentro de tu JFrame principal
            int x = this.getX() + (this.getWidth() - borrado.getWidth()) / 2;
            int y = this.getY() + (this.getHeight() - borrado.getHeight()) / 2;
            borrado.setLocation(x, y);

            this.setEnabled(false);  // Desactivar la ventana principal mientras se muestra la de confirmación

            // Reactivar la ventana principal cuando se cierre la de confirmación
            borrado.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    setEnabled(true);  // Reactivar la ventana principal
                    mostrarDatos();
                    btnBorrar.setEnabled(false);
                    btnEditar.setEnabled(false);
                }
            });
            objConexion.closeConnection();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para borrar.");
        }
    }//GEN-LAST:event_btnBorrarActionPerformed

    // EVENTO CLICK DEL BOTÓN LIBROS ----------------------------------------------------------------------------------------------------
    private void btnVerTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerTodosActionPerformed
        panelVerTodos.setVisible(true);
        panelEditar.setVisible(false);
        panelAgregar.setVisible(false);

        btnVerTodos.setEnabled(false);
        btnAgregar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnBorrar.setEnabled(false);

        mostrarDatos();
    }//GEN-LAST:event_btnVerTodosActionPerformed
    // ----------------------------------------------------------------------------------------------------------------------------------

    // EVENTO CLICK DEL BOTÓN SALIR ----------------------------------------------------------------------------------------------------- 
    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        PreguntaSalir salir = new PreguntaSalir();
        salir.setVisible(true);
        // Calcula la posición para centrarla dentro de tu JFrame principal
        int x = this.getX() + (this.getWidth() - salir.getWidth()) / 2;
        int y = this.getY() + (this.getHeight() - salir.getHeight()) / 2;
        salir.setLocation(x, y);

        this.setEnabled(false);

        // Para reactivar el formulario principal cuando se cierre el diálogo
        salir.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                setEnabled(true);  // Reactivar el formulario principal
            }
        });
    }//GEN-LAST:event_btnSalirActionPerformed
    // ----------------------------------------------------------------------------------------------------------------------------------

    // EVENTO CLICK DEL BOTÓN CARGAR PORTADA -------------------------------------------------------------------------------------------- 
    private void btnCargarPortadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarPortadaActionPerformed
        JFileChooser jFileChooser = new JFileChooser();
        FileNameExtensionFilter filtrado = new FileNameExtensionFilter("JPG & PNG", "jpg", "png");
        jFileChooser.setFileFilter(filtrado);

        int respuesta = jFileChooser.showOpenDialog(this);

        if (respuesta == JFileChooser.APPROVE_OPTION) {
            // Obtener el archivo seleccionado
            File archivoSeleccionado = jFileChooser.getSelectedFile();
            rutaImagenSeleccionada = archivoSeleccionado.getPath(); // Guardar la ruta

            // Mostrar la imagen en el JLabel
            Image mImagen = new ImageIcon(rutaImagenSeleccionada).getImage();
            ImageIcon mIcono = new ImageIcon(mImagen.getScaledInstance(lblPortada.getWidth(), lblPortada.getHeight(), Image.SCALE_DEFAULT));
            lblPortada.setIcon(mIcono);
        }
    }//GEN-LAST:event_btnCargarPortadaActionPerformed
    // ----------------------------------------------------------------------------------------------------------------------------------

    // EVENTO CLICK DEL BOTÓN GUARDAR LIBRO ----------------------------------------------------------------------------------------------- 
    private void btnGuardarAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarAgregarActionPerformed
        nombreArchivoNuevo = null;
        // Guardamos la imagen de la portada en la carpeta "portadas"
        if (rutaImagenSeleccionada != null) {
            // Obtener la ruta del directorio del usuario
            String userHome = System.getProperty("user.home");

            // Definir la carpeta destino en la ruta del sistema del usuario
            String carpetaDestino = userHome + "/Mi Biblioteca/portadas/";

            // Crear el directorio "portadas" si no existe
            File directorio = new File(carpetaDestino);
            if (!directorio.exists()) {
                directorio.mkdirs(); // Crear la carpeta si no existe
            }

            // Obtener el nombre del archivo original (imagen seleccionada)
            File archivoOriginal = new File(rutaImagenSeleccionada);

            // Obtener el nombre del archivo original
            String nombreArchivoOriginal = archivoOriginal.getName();

            // Extraer la extensión del archivo (por ejemplo, .jpg o .png)
            String extension = nombreArchivoOriginal.substring(nombreArchivoOriginal.lastIndexOf("."));

            // Usar el texto del textField txtISBN como el nuevo nombre del archivo
            nombreArchivoNuevo = txtISBN.getText() + extension;
            //System.out.println("Nombre archivo= " + nombreArchivoNuevo);

            // Ruta completa del nuevo archivo en la carpeta de destino
            File archivoDestino = new File(carpetaDestino + nombreArchivoNuevo);

            // Copiar el archivo a la nueva ubicación
            try {
                Files.copy(archivoOriginal.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                System.out.println("Error al copiar el archivo: " + ex.getMessage());
            }
        } else {
            // Si no se selecciona ninguna imagen, asignar un archivo por defecto
            nombreArchivoNuevo = "portadaLibro.png";
        }
        // Limpiar la variable rutaImagenSeleccionada después de usarla
        rutaImagenSeleccionada = null;

        // Comprobamos si ha marcado la opción "Empezado hoy".
        Date fecha;
        String fechaFormateada = "";
        String estadoLectura = "Pendiente";
        if (checkEmpezadoHoy.isSelected()) {
            fecha = new Date(System.currentTimeMillis());
            // Formatear la fecha
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            fechaFormateada = formato.format(fecha);
            estadoLectura = "En progreso";
        } else {
            fecha = null;
        }

        Conexion objConexion = new Conexion();

        Libro oLibro = recuperarDatosGUI();
        String strSentenciaInsert = String.format("INSERT INTO libros (isbn, titulo, autor, genero, fecha_publicacion, numero_paginas, formato, editorial, saga, idioma, sinopsis, portada, fecha_inicio, estado_lectura) "
                + "VALUES ('%s', '%s', '%s', '%s', %d, %d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                oLibro.getIsbn(), oLibro.getTitulo(), oLibro.getAutor(), oLibro.getGenero(), oLibro.getFecha_publicacion(), oLibro.getNumero_paginas(), oLibro.getFormato(), oLibro.getEditorial(), oLibro.getSaga(), oLibro.getIdioma(), oLibro.getSinopsis(), nombreArchivoNuevo, fechaFormateada, estadoLectura);

        // Reiniciacmos la variable nombreArchivoNuevo a por defecto.
        nombreArchivoNuevo = "portadaLibro.png";

        int respuesta = objConexion.ejecutarSentenciaSQL(strSentenciaInsert);
        if (respuesta == 1) {
            VentanaTodoOK todoOk = new VentanaTodoOK();
            todoOk.setVisible(true);
            // Calcula la posición para centrarla dentro de tu JFrame principal
            int x = this.getX() + (this.getWidth() - todoOk.getWidth()) / 2;
            int y = this.getY() + (this.getHeight() - todoOk.getHeight()) / 2;
            todoOk.setLocation(x, y);

            this.setEnabled(false);
            // Para reactivar el formulario principal cuando se cierre el diálogo
            todoOk.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    setEnabled(true);  // Reactivar el formulario principal
                    mostrarDatos();
                    panelAgregar.setVisible(false);
                    panelVerTodos.setVisible(true);
                    btnAgregar.setEnabled(true);
                    btnVerTodos.setEnabled(false);
                }
            });
            this.limpiarAgregar();
            this.mostrarDatos();
        }
    }//GEN-LAST:event_btnGuardarAgregarActionPerformed
    // ----------------------------------------------------------------------------------------------------------------------------------

    // EVENTO CLICK DEL BOTÓN LIMPIAR DEL FORMULARIO DE AGREGAR -------------------------------------------------------------------------
    private void btnLimpiarAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarAgregarActionPerformed
        this.limpiarAgregar();
    }//GEN-LAST:event_btnLimpiarAgregarActionPerformed
    // ----------------------------------------------------------------------------------------------------------------------------------

    // EVENTO CLICK DE ALGÚN ELEMENTO DENTRO DE LA TABLA --------------------------------------------------------------------------------
    private void tblTablaLibrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTablaLibrosMouseClicked
        if (evt.getClickCount() == 1) {
            JTable receptor = (JTable) evt.getSource();

            btnBorrar.setEnabled(true);
            btnEditar.setEnabled(true);
            btnVerTodos.setEnabled(true);
        }
    }//GEN-LAST:event_tblTablaLibrosMouseClicked
    // ----------------------------------------------------------------------------------------------------------------------------------

    private void btnCargarPortadaEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarPortadaEditarActionPerformed
        JFileChooser jFileChooser = new JFileChooser();
        FileNameExtensionFilter filtrado = new FileNameExtensionFilter("JPG & PNG", "jpg", "png");
        jFileChooser.setFileFilter(filtrado);

        int respuesta = jFileChooser.showOpenDialog(this);

        if (respuesta == JFileChooser.APPROVE_OPTION) {
            // Obtener el archivo seleccionado
            File archivoSeleccionado = jFileChooser.getSelectedFile();
            rutaImagenSeleccionada = archivoSeleccionado.getPath(); // Guardar la ruta

            // Mostrar la imagen en el JLabel
            Image mImagen = new ImageIcon(rutaImagenSeleccionada).getImage();
            ImageIcon mIcono = new ImageIcon(mImagen.getScaledInstance(176, 261, Image.SCALE_DEFAULT));
            lblPortadaEditar.setIcon(mIcono);

            // Guardar la extensión de la imagen
            String extension = getExtension(archivoSeleccionado.getName());
            // Establecer el nombre de la portada, por ejemplo, ISBN + extensión
            nombrePortada = txtISBNEditar.getText() + "." + extension; // Almacena el nombre de la portada

        }
    }//GEN-LAST:event_btnCargarPortadaEditarActionPerformed
    // ----------------------------------------------------------------------------------------------------------------------------------

    // EVENTO CLICK DEL BOTÓN GUARDAR DEL PANEL DE EDITAR -------------------------------------------------------------------------------
    private void btnGuardarEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarEditarActionPerformed
        Conexion objConexion = new Conexion();
        Libro oLibro = recuperarDatosEditarGUI();

        // Se copia la imagen en la carpeta de Portadas.
        nombreArchivoNuevo = null;

        // Guardamos la imagen de la portada en la carpeta "portadas"
        if (rutaImagenSeleccionada != null) {
            // (El código para copiar la imagen se mantiene igual)
            // Obtener la ruta del directorio del usuario
            String userHome = System.getProperty("user.home");
            // Definir la carpeta destino en la ruta del sistema del usuario
            String carpetaDestino = userHome + "/Mi Biblioteca/portadas/";
            // Crear el directorio "portadas" si no existe
            File directorio = new File(carpetaDestino);
            if (!directorio.exists()) {
                directorio.mkdirs(); // Crear la carpeta si no existe
            }

            // Obtener el nombre del archivo original
            File archivoOriginal = new File(rutaImagenSeleccionada);
            String nombreArchivoOriginal = archivoOriginal.getName();
            String extension = nombreArchivoOriginal.substring(nombreArchivoOriginal.lastIndexOf("."));

            // Usar el texto del textField txtISBN como el nuevo nombre del archivo
            nombreArchivoNuevo = oLibro.getIsbn() + extension;

            // Ruta completa del nuevo archivo en la carpeta de destino
            File archivoDestino = new File(carpetaDestino + nombreArchivoNuevo);

            // Copiar el archivo a la nueva ubicación
            try {
                Files.copy(archivoOriginal.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                System.out.println("Error al copiar el archivo: " + ex.getMessage());
            }
        }

        String strSentenciaUpdate = "";
        if (nombreArchivoNuevo != null) {
            // Se actualiza el campo portada solo si hay un nuevo archivo
            strSentenciaUpdate = String.format("UPDATE libros SET "
                    + "titulo = '%s', "
                    + "autor = '%s', "
                    + "genero = '%s', "
                    + "fecha_publicacion = %d, "
                    + "numero_paginas = %d, "
                    + "formato = '%s', "
                    + "editorial = '%s', "
                    + "saga = '%s', "
                    + "idioma = '%s', "
                    + "sinopsis = '%s', "
                    + "portada = '%s', " // Solo se asigna si hay una nueva portada
                    + "fecha_inicio = %s, "
                    + "fecha_fin = %s, "
                    + "calificacion = %s "
                    + "WHERE isbn = '%s'",
                    oLibro.getTitulo(),
                    oLibro.getAutor(),
                    oLibro.getGenero(),
                    oLibro.getFecha_publicacion(),
                    oLibro.getNumero_paginas(),
                    oLibro.getFormato(),
                    oLibro.getEditorial(),
                    oLibro.getSaga(),
                    oLibro.getIdioma(),
                    oLibro.getSinopsis(),
                    nombreArchivoNuevo, // Se actualiza solo si hay una nueva portada
                    (oLibro.getFecha_inicio() == null ? "NULL" : "'" + new SimpleDateFormat("yyyy-MM-dd").format(oLibro.getFecha_inicio()) + "'"),
                    (oLibro.getFecha_fin() == null ? "NULL" : "'" + new SimpleDateFormat("yyyy-MM-dd").format(oLibro.getFecha_fin()) + "'"),
                    (oLibro.getCalificacion() == null ? "NULL" : oLibro.getCalificacion()),
                    oLibro.getIsbn());
        } else {
            // En este caso, no se actualiza el campo portada
            strSentenciaUpdate = String.format("UPDATE libros SET "
                    + "titulo = '%s', "
                    + "autor = '%s', "
                    + "genero = '%s', "
                    + "fecha_publicacion = %d, "
                    + "numero_paginas = %d, "
                    + "formato = '%s', "
                    + "editorial = '%s', "
                    + "saga = '%s', "
                    + "idioma = '%s', "
                    + "sinopsis = '%s', "
                    + "fecha_inicio = %s, "
                    + "fecha_fin = %s, "
                    + "calificacion = %s "
                    + "WHERE isbn = '%s'",
                    oLibro.getTitulo(),
                    oLibro.getAutor(),
                    oLibro.getGenero(),
                    oLibro.getFecha_publicacion(),
                    oLibro.getNumero_paginas(),
                    oLibro.getFormato(),
                    oLibro.getEditorial(),
                    oLibro.getSaga(),
                    oLibro.getIdioma(),
                    oLibro.getSinopsis(),
                    (oLibro.getFecha_inicio() == null ? "NULL" : "'" + new SimpleDateFormat("yyyy-MM-dd").format(oLibro.getFecha_inicio()) + "'"),
                    (oLibro.getFecha_fin() == null ? "NULL" : "'" + new SimpleDateFormat("yyyy-MM-dd").format(oLibro.getFecha_fin()) + "'"),
                    (oLibro.getCalificacion() == null ? "NULL" : oLibro.getCalificacion()),
                    oLibro.getIsbn());
        }

        int respuesta = objConexion.ejecutarSentenciaSQL(strSentenciaUpdate);
        if (respuesta == 1) {
            VentanaTodoOKUpdate todoOk = new VentanaTodoOKUpdate();
            todoOk.setVisible(true);
            // Calcula la posición para centrarla dentro de tu JFrame principal
            int x = this.getX() + (this.getWidth() - todoOk.getWidth()) / 2;
            int y = this.getY() + (this.getHeight() - todoOk.getHeight()) / 2;
            todoOk.setLocation(x, y);

            this.setEnabled(false);
            // Para reactivar el formulario principal cuando se cierre el diálogo
            todoOk.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    setEnabled(true);  // Reactivar el formulario principal
                    mostrarDatos();
                    panelVerTodos.setVisible(true);
                    panelAgregar.setVisible(false);
                    panelEditar.setVisible(false);

                    btnAgregar.setEnabled(true);
                    btnVerTodos.setEnabled(false);
                    btnEditar.setEnabled(false);
                }
            });
            //this.limpiarEditar();
            objConexion.closeConnection();
        }
    }//GEN-LAST:event_btnGuardarEditarActionPerformed
    // ----------------------------------------------------------------------------------------------------------------------------------

    // MÉTODO PARA LIMPIAR EL FORMULARIO DE EDITAR UN LIBRO ---------------------------------------------------------------------------
    private void btnLimpiarEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarEditarActionPerformed
        limpiarEditar();
    }//GEN-LAST:event_btnLimpiarEditarActionPerformed

    private void btnPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPdfActionPerformed
        LibroDAO libroDAO = new LibroDAO();
        List<Libro> libros = libroDAO.obtenerLibros();

        // Verificar que se obtuvieron libros
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos.");
            return; // Salir si no hay libros
        }

        // Definir la ruta de la carpeta y el archivo PDF
        String userHome = System.getProperty("user.home");
        String outputPath = userHome + "/Mi Biblioteca/pdf/listadoLibros.pdf";

        // Crear la carpeta si no existe
        File pdfDir = new File(userHome + "/Mi Biblioteca/pdf");
        if (!pdfDir.exists()) {
            pdfDir.mkdirs(); // Crea las carpetas si no existen
        }

        generatePdf(libros, outputPath);

        // Cerrar la conexión
        libroDAO.cerrarConexion();

        VentanaPDF pdf = new VentanaPDF();
        pdf.setVisible(true);
        // Calcula la posición para centrarla dentro de tu JFrame principal
            int x = this.getX() + (this.getWidth() - pdf.getWidth()) / 2;
            int y = this.getY() + (this.getHeight() - pdf.getHeight()) / 2;
            pdf.setLocation(x, y);
        pdf.setResizable(false);

        this.setEnabled(false);

        // Para reactivar el formulario principal cuando se cierre el diálogo
        pdf.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                setEnabled(true);  // Reactivar el formulario principal
            }
        });
    }//GEN-LAST:event_btnPdfActionPerformed
    // ----------------------------------------------------------------------------------------------------------------------------------

    // MÉTODO PARA MOSTRAR LOS REGISTROS EN LA TABLA ------------------------------------------------------------------------------------
    // Procedimiento que muestra los registros de la base de datos en la tabla.
    public void mostrarDatos() {
        modelo.setRowCount(0);
        Conexion objConexion = new Conexion();

        // HashMaps para almacenar autores y géneros con su conteo
        HashMap<String, Integer> conteoAutores = new HashMap<>();
        HashMap<String, Integer> conteoGeneros = new HashMap<>();

        try {
            ResultSet resultado = objConexion.consultarRegistros("SELECT * FROM libros");

            // Verificar si hay resultados
            if (!resultado.isBeforeFirst()) { // Si el resultado está vacío
                lblAutorFavorito.setText("Aún no tienes ningún autor favorito.");
                lblGeneroFavorito.setText("Aún no tienes ningún género favorito.");
                return; // Salir del método
            }

            while (resultado.next()) {
                String estado; // Variable para almacenar el estado
                String fechaInicio = resultado.getString("fecha_inicio");
                String fechaFin = resultado.getString("fecha_fin");

                // Evaluar el estado según la lógica
                if (fechaInicio == null || fechaInicio.trim().isEmpty()) {
                    estado = "Pendiente"; // No hay fecha de inicio
                } else if (fechaFin == null || fechaFin.trim().isEmpty()) {
                    estado = "Progreso"; // Hay fecha de inicio pero no fin
                } else {
                    estado = "Terminado"; // Hay ambas fechas
                }

                // Obtener autor y género
                String autor = resultado.getString("autor");
                String genero = resultado.getString("genero");

                // Incrementar el conteo del autor
                conteoAutores.put(autor, conteoAutores.getOrDefault(autor, 0) + 1);

                // Incrementar el conteo del género
                conteoGeneros.put(genero, conteoGeneros.getOrDefault(genero, 0) + 1);

                Object[] oLibro = {
                    resultado.getString("id_libro"),
                    resultado.getString("isbn"),
                    resultado.getString("titulo"),
                    autor,
                    genero,
                    resultado.getString("numero_paginas"),
                    estado
                };
                modelo.addRow(oLibro);
            }

            resultado.close();

            // Buscar el autor favorito (el que más se repite)
            String autorFavorito = obtenerMasRepetido(conteoAutores);
            String generoFavorito = obtenerMasRepetido(conteoGeneros);

            // Mostrar el autor favorito
            if (autorFavorito != null) {
                lblAutorFavorito.setText("<html><span style='color:#f1be1e;'>" + autorFavorito + "</span> es tu autor favorito</html>");
            } else {
                lblAutorFavorito.setText("Aún no tienes ningún autor favorito.");
            }

            // Mostrar el género favorito
            if (generoFavorito != null) {
                lblGeneroFavorito.setText("<html><span style='color:#f1be1e;'>" + generoFavorito + "</span> es tu género favorito</html>");
            } else {
                lblGeneroFavorito.setText("Aún no tienes ningún género favorito.");
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            objConexion.closeConnection(); // Asegurarse de cerrar la conexión
        }
    }

    /*
     * Método auxiliar para obtener el elemento más repetido en un HashMap.
     */
    private String obtenerMasRepetido(HashMap<String, Integer> conteo) {
        String masRepetido = null;
        int maxRepeticiones = 0;

        for (Map.Entry<String, Integer> entry : conteo.entrySet()) {
            if (entry.getValue() > maxRepeticiones) {
                maxRepeticiones = entry.getValue();
                masRepetido = entry.getKey();
            }
        }

        return masRepetido;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------
    // MÉTODO PARA LIMPIAR EL FORMULARIO DE AGREGAR UN LIBRO ---------------------------------------------------------------------------
    private void limpiarAgregar() {
        // Guardar los filtros actuales
        DocumentFilter isbnFilter = ((PlainDocument) txtISBN.getDocument()).getDocumentFilter();
        DocumentFilter paginasFilter = ((PlainDocument) txtPaginas.getDocument()).getDocumentFilter();
        DocumentFilter fechaFilter = ((PlainDocument) txtFechaPublicacion.getDocument()).getDocumentFilter();

        // Desactivar temporalmente los filtros
        ((PlainDocument) txtISBN.getDocument()).setDocumentFilter(null);
        ((PlainDocument) txtPaginas.getDocument()).setDocumentFilter(null);
        ((PlainDocument) txtFechaPublicacion.getDocument()).setDocumentFilter(null);

        // Limpiar todos los textfields
        txtISBN.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtGenero.setText("");
        txtFechaPublicacion.setText("");
        txtPaginas.setText("");
        cbFormato.setSelectedIndex(0);
        txtEditorial.setText("");
        txtSaga.setText("");
        cbIdioma.setSelectedIndex(0);
        checkEmpezadoHoy.setSelected(false);
        txtSinopsis.setText("");

        // Cargar imagen portada por defecto
        String userHome = System.getProperty("user.home");
        String carpetaDestino = userHome + "/Mi Biblioteca/portadas/portadaLibro.png";
        lblPortada.setIcon(new javax.swing.ImageIcon(carpetaDestino));

        // Restaurar los filtros originales
        ((PlainDocument) txtISBN.getDocument()).setDocumentFilter(isbnFilter);
        ((PlainDocument) txtPaginas.getDocument()).setDocumentFilter(paginasFilter);
        ((PlainDocument) txtFechaPublicacion.getDocument()).setDocumentFilter(fechaFilter);
    }
    // ---------------------------------------------------------------------------------------------------------------------------------

    // MÉTODO PARA LIMPIAR EL FORMULARIO DE EDITAR UN LIBRO ---------------------------------------------------------------------------
    private void limpiarEditar() {
        Conexion objConexion = new Conexion();

        int filaSeleccionada = tblTablaLibros.getSelectedRow(); // Obtener la fila seleccionada
        if (filaSeleccionada != -1) { // Verificar que hay una fila seleccionada
            // Obtener el valor de ISBN de la primera columna
            String idLibro = tblTablaLibros.getModel().getValueAt(filaSeleccionada, 0).toString();

            // Crear la sentencia SELECT utilizando el valor de ISBN
            String strSentenciaSelect = String.format("SELECT * FROM libros WHERE id_libro = %s", idLibro);

            // Ejecutar la consulta.
            ResultSet resultadoConsulta = objConexion.consultarRegistros(strSentenciaSelect);

            try {
                if (resultadoConsulta.next()) {
                    // Crear un objeto Libro y rellenarlo con los datos del ResultSet
                    Libro oLibro = new Libro();
                    oLibro.setId_libro(resultadoConsulta.getInt("id_libro"));
                    oLibro.setIsbn(resultadoConsulta.getString("isbn"));
                    oLibro.setTitulo(resultadoConsulta.getString("titulo"));
                    oLibro.setAutor(resultadoConsulta.getString("autor"));
                    oLibro.setGenero(resultadoConsulta.getString("genero"));

                    // Fecha Inicio --------------------------------------------
                    String fechaInicioStr = resultadoConsulta.getString("fecha_inicio");
                    Date fechaInicio = null; // Inicializa la variable

                    // Verifica que fechaInicioStr no sea null y no esté vacío
                    if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            fechaInicio = formatoFecha.parse(fechaInicioStr);
                            oLibro.setFecha_inicio(fechaInicio);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        oLibro.setFecha_inicio(null); // Establece a null si la cadena está vacía o es null
                    }

                    // Muestra la fecha en el JTextField en el formato dd/MM/yyyy
                    if (fechaInicio != null) {

                        // Guardar los filtros actuales
                        ((AbstractDocument) txtFechaInicioEditar.getDocument()).setDocumentFilter(new DateNumericDocumentFilter(11));
                        // Desactivar temporalmente los filtros
                        ((AbstractDocument) txtFechaInicioEditar.getDocument()).setDocumentFilter(null);

                        SimpleDateFormat formatoFechaMostrar = new SimpleDateFormat("dd/MM/yyyy");
                        String fechaInicioEditarStr = formatoFechaMostrar.format(fechaInicio);
                        txtFechaInicioEditar.setText(fechaInicioEditarStr);
                    } else {
                        txtFechaInicioEditar.setText(""); // Asegúrate de que el JTextField esté vacío si no hay fecha
                    }
                    ((AbstractDocument) txtFechaInicioEditar.getDocument()).setDocumentFilter(new DateNumericDocumentFilter(11));
                    // ---------------------------------------------------------

                    // Fecha Final --------------------------------------------
                    String fechaFinalStr = resultadoConsulta.getString("fecha_fin");
                    java.util.Date fechaFinal = null; // Inicializa la variable
                    // Guardar los filtros actuales
                    ((AbstractDocument) txtFechaFinEditar.getDocument()).setDocumentFilter(new DateNumericDocumentFilter(11));
                    // Desactivar temporalmente los filtros
                    ((AbstractDocument) txtFechaFinEditar.getDocument()).setDocumentFilter(null);

                    // Verifica que fechaFinalStr no sea null y no esté vacío
                    if (fechaFinalStr != null && !fechaFinalStr.isEmpty()) {
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            fechaFinal = formatoFecha.parse(fechaFinalStr);
                            oLibro.setFecha_fin(fechaFinal);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        oLibro.setFecha_fin(null); // Establece a null si la cadena está vacía o es null
                    }

                    // Muestra la fecha en el JTextField en el formato dd/MM/yyyy
                    if (fechaFinal != null) { // Asegúrate de usar fechaFinal aquí
                        SimpleDateFormat formatoFechaMostrar = new SimpleDateFormat("dd/MM/yyyy");
                        String fechaFinEditarStr = formatoFechaMostrar.format(fechaFinal);
                        txtFechaFinEditar.setText(fechaFinEditarStr); // Usa txtFechaFinEditar
                    } else {
                        txtFechaFinEditar.setText(""); // Asegúrate de que el JTextField esté vacío si no hay fecha
                    }
                    ((AbstractDocument) txtFechaFinEditar.getDocument()).setDocumentFilter(new DateNumericDocumentFilter(11));
                    // ---------------------------------------------------------

                    oLibro.setEstado_lectura(resultadoConsulta.getString("estado_lectura"));
                    oLibro.setCalificacion(resultadoConsulta.getInt("calificacion"));
                    oLibro.setSinopsis(resultadoConsulta.getString("sinopsis"));
                    oLibro.setNumero_paginas(resultadoConsulta.getInt("numero_paginas"));
                    oLibro.setFormato(resultadoConsulta.getString("formato"));
                    oLibro.setIdioma(resultadoConsulta.getString("idioma"));
                    oLibro.setFecha_publicacion(resultadoConsulta.getInt("fecha_publicacion"));
                    oLibro.setEditorial(resultadoConsulta.getString("editorial"));
                    oLibro.setSaga(resultadoConsulta.getString("saga"));
                    oLibro.setPortada(resultadoConsulta.getString("portada"));

                    txtISBNEditar.setText(oLibro.getIsbn());
                    txtTituloEditar.setText(oLibro.getTitulo());
                    txtAutorEditar.setText(oLibro.getAutor());
                    txtGeneroEditar.setText(oLibro.getGenero());
                    txtEditorialEditar.setText(oLibro.getEditorial());
                    if (oLibro.getSaga() != null) {
                        txtSagaEditar.setText(oLibro.getSaga());
                    } else {
                        txtSagaEditar.setText("");
                    }
                    txtFechaPublicacionEditar.setText(String.valueOf(oLibro.getFecha_publicacion()));
                    txtPaginasEditar.setText(String.valueOf(oLibro.getNumero_paginas()));
                    cbFormatoEditar.setSelectedItem(oLibro.getFormato());
                    cbIdiomaEditar.setSelectedItem(oLibro.getIdioma());
                    if (oLibro.getCalificacion() != null) {
                        txtCalificacionEditar.setText(String.valueOf(oLibro.getCalificacion()));
                    } else {
                        txtCalificacionEditar.setText(null);  // Campo vacío si la calificación es null
                    }
                    txtSinopsisEditar.setText(String.valueOf(oLibro.getSinopsis()));

                    // Portada del libro ---------------------------------------
                    String nombrePortada = oLibro.getPortada(); // Debes asegurarte de que no sea null

                    if (nombrePortada != null && !nombrePortada.isEmpty()) {
                        // Construye la ruta completa de la imagen
                        // Obtener la ruta del directorio del usuario
                        String userHome = System.getProperty("user.home");

                        // Definir la carpeta destino en la ruta del sistema del usuario
                        String carpetaDestino = userHome + "/Mi Biblioteca/portadas/";

                        // Construir la ruta completa de la imagen
                        String imageUrl = carpetaDestino + nombrePortada;

                        if (imageUrl != null) {
                            // Carga la imagen desde la URL
                            ImageIcon originalIcon = new ImageIcon(imageUrl);
                            Image originalImage = originalIcon.getImage();

                            // Escala la imagen al tamaño del JLabel
                            Image scaledImage = originalImage.getScaledInstance(176, 261, Image.SCALE_DEFAULT);

                            // Establece la imagen escalada como ícono en el JLabel
                            lblPortadaEditar.setIcon(new ImageIcon(scaledImage));
                        } else {
                            // Maneja el caso donde la imagen no se encuentra
                            mostrarImagenPorDefecto(lblPortadaEditar); // Llamar al método que muestra la imagen por defecto
                        }
                    } else {
                        // Maneja el caso donde la imagen no se encuentra
                        mostrarImagenPorDefecto(lblPortadaEditar); // Llamar al método que muestra la imagen por defecto
                    }
                    // ---------------------------------------------------------
                    // Agregar el ItemListener al JCheckBox
                    checkTerminadoHoy.addItemListener(new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent e) {
                            if (checkTerminadoHoy.isSelected()) {

                                // Guardar los filtros actuales
                                ((AbstractDocument) txtFechaFinEditar.getDocument()).setDocumentFilter(new DateNumericDocumentFilter(11));
                                // Desactivar temporalmente los filtros
                                ((AbstractDocument) txtFechaFinEditar.getDocument()).setDocumentFilter(null);

                                // Obtener la fecha actual
                                String fechaHoy = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                                txtFechaFinEditar.setText(fechaHoy);

                            } else {
                                // Vaciar el JTextField
                                txtFechaFinEditar.setText("");
                            }
                            // Restaurar los filtros originales
                            ((AbstractDocument) txtFechaFinEditar.getDocument()).setDocumentFilter(new DateNumericDocumentFilter(11));
                        }
                    });
                    resultadoConsulta.close();
                    objConexion.closeConnection();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
    // ---------------------------------------------------------------------------------------------------------------------------------

    // FUNCIÓN QUE RECUPERA LOS DATOS DE LA INTERFAZ GRÁFICA EN EL PANEL AGREGAR -------------------------------------------------------
    public Libro recuperarDatosGUI() {
        Libro oLibro = new Libro();

        oLibro.setIsbn(txtISBN.getText());
        oLibro.setTitulo(txtTitulo.getText());
        oLibro.setAutor(txtAutor.getText());
        oLibro.setGenero(txtGenero.getText());
        oLibro.setFecha_publicacion(Integer.parseInt(txtFechaPublicacion.getText()));
        oLibro.setNumero_paginas(Integer.parseInt(txtPaginas.getText()));
        oLibro.setFormato(cbFormato.getSelectedItem().toString());
        oLibro.setEditorial(txtEditorial.getText());
        oLibro.setSaga(txtSaga.getText());
        oLibro.setIdioma(cbIdioma.getSelectedItem().toString());
        oLibro.setSinopsis(txtSinopsis.getText());

        return oLibro;
    }
    // ---------------------------------------------------------------------------------------------------------------------------------

    // FUNCIÓN QUE RECUPERA LOS DATOS INTRODUCIDOS DESDE LA INTERFAZ GRÁFICA EN EL PANEL EDITAR.
    public Libro recuperarDatosEditarGUI() {
        Libro oLibro = new Libro();

        oLibro.setIsbn(txtISBNEditar.getText());
        oLibro.setTitulo(txtTituloEditar.getText());
        oLibro.setAutor(txtAutorEditar.getText());
        oLibro.setGenero(txtGeneroEditar.getText());
        oLibro.setFecha_publicacion(Integer.parseInt(txtFechaPublicacionEditar.getText()));
        oLibro.setNumero_paginas(Integer.parseInt(txtPaginasEditar.getText()));
        oLibro.setFormato(cbFormatoEditar.getSelectedItem().toString());
        oLibro.setEditorial(txtEditorialEditar.getText());
        oLibro.setSaga(txtSagaEditar.getText());
        oLibro.setIdioma(cbIdiomaEditar.getSelectedItem().toString());
        oLibro.setSinopsis(txtSinopsisEditar.getText());

        // Obtener el texto del JTextField
        String calificacionText = txtCalificacionEditar.getText();

        // Verificar si el campo está vacío
        if (calificacionText.isEmpty()) {
            // Manejar el caso en que el campo está vacío
            oLibro.setCalificacion(0); // Asignar un valor por defecto
        } else {
            try {
                // Intentar convertir el texto a un entero
                int calificacion = Integer.parseInt(calificacionText);
                // Asignar la calificación al objeto oLibro
                oLibro.setCalificacion(calificacion);
            } catch (NumberFormatException e) {
                System.out.println(e);
            }
        }

        // Asignar el nombre de la portada almacenada temporalmente
        oLibro.setPortada(nombrePortada);

        try {
            // Crear el formato en el que está la fecha en los JTextField (por ejemplo, dd/MM/yyyy)
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

            // Obtener el texto de los JTextField y convertirlo en tipo Date
            String fechaInicioStr = txtFechaInicioEditar.getText();
            String fechaFinStr = txtFechaFinEditar.getText();

            // Verificar que los campos no estén vacíos
            if (!fechaInicioStr.isEmpty()) {
                Date fechaInicio = formatoFecha.parse(fechaInicioStr); // Convierte el String en Date
                oLibro.setFecha_inicio(fechaInicio); // Asigna la fecha al objeto
            } else {
                oLibro.setFecha_inicio(null); // Si está vacío, asigna null
            }

            if (!fechaFinStr.isEmpty()) {
                Date fechaFin = formatoFecha.parse(fechaFinStr); // Convierte el String en Date
                oLibro.setFecha_fin(fechaFin); // Asigna la fecha al objeto
            } else {
                oLibro.setFecha_fin(null); // Si está vacío, asigna null
            }

        } catch (ParseException e) {
            e.printStackTrace(); // Manejar el error si la fecha no está en el formato correcto
            oLibro.setFecha_inicio(null); // Asegúrate de asignar null si ocurre un error
            oLibro.setFecha_fin(null); // Asegúrate de asignar null si ocurre un error
        }

        return oLibro;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------
    // ESTE MÉTODO SE ENCARGARÁ DE REVISAR SI TODOS LOS CAMPOS ESTÁN COMPLETOS Y SI EL LBLPORTADA TIENE UNA IMAGEN ---------------------
    private void verificarCamposYActivarBoton() {
        boolean camposCompletos = !txtISBN.getText().trim().isEmpty()
                && !txtTitulo.getText().trim().isEmpty()
                && !txtAutor.getText().trim().isEmpty()
                && !txtGenero.getText().trim().isEmpty()
                && !txtFechaPublicacion.getText().trim().isEmpty()
                && !txtPaginas.getText().trim().isEmpty()
                && !txtEditorial.getText().trim().isEmpty()
                && !txtSinopsis.getText().trim().isEmpty()
                && lblPortada.getIcon() != null; // Verificar si hay una imagen

        // Si todos los campos están completos y hay imagen, activa el botón
        btnGuardarAgregar.setEnabled(camposCompletos);

        boolean isFechaInicioValida = isDateValid(txtFechaInicioEditar.getText());
        boolean isFechaFinValida = isDateValid(txtFechaFinEditar.getText());

        // Habilitar el botón si:
        // 1. Ambas fechas están vacías
        // 2. Ambas fechas son válidas
        // 3. Una fecha es válida y la otra está vacía
        boolean botonHabilitado = (txtFechaInicioEditar.getText().isEmpty() && txtFechaFinEditar.getText().isEmpty())
                || (isFechaInicioValida && (isFechaFinValida || txtFechaFinEditar.getText().isEmpty()))
                || (isFechaFinValida && (isFechaInicioValida || txtFechaInicioEditar.getText().isEmpty()));

        btnGuardarEditar.setEnabled(botonHabilitado);
    }
    // ---------------------------------------------------------------------------------------------------------------------------------

    // MÉTODO PARA MOSTRAR LA IMAGEN POR DEFECTO.
    private void mostrarImagenPorDefecto(JLabel lblPortadaEditar) {
        // Obtener la ruta del directorio del usuario
        String userHome = System.getProperty("user.home");
        String carpetaDestino = userHome + "/Mi Biblioteca/portadas/";
        String imageUrlPorDefecto = carpetaDestino + "portadaLibro.png";
        //System.out.println("RUTA IMAGEEEEEN: " + imageUrlPorDefecto);

        // Cargar la imagen por defecto
        ImageIcon defaultIcon = new ImageIcon(imageUrlPorDefecto);
        Image defaultImage = defaultIcon.getImage();

        // Escalar la imagen por defecto
        Image scaledDefaultImage = defaultImage.getScaledInstance(176, 261, Image.SCALE_DEFAULT);
        lblPortadaEditar.setIcon(new ImageIcon(scaledDefaultImage));
    }
    // ---------------------------------------------------------------------------------------------------------------------------------

    // Método para obtener la extensión del archivo
    private String getExtension(String filename) {
        int lastIndexOfDot = filename.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return ""; // Sin extensión
        }
        return filename.substring(lastIndexOfDot + 1).toLowerCase(); // Retorna la extensión en minúscula
    }

    // Método adicional para almacenar la extensión de la imagen cargada
    private String getImageExtension() {
        if (rutaImagenSeleccionada != null) {
            return getExtension(new File(rutaImagenSeleccionada).getName());
        }
        return ""; // Retornar vacío si no hay imagen seleccionada
    }

    private boolean isDateValid(String date) {
        // Verificar si el formato es correcto
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            return false; // Formato no válido
        }

        // Intentar parsear la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); // Desactivar la leniencia
        try {
            sdf.parse(date); // Verificar si la fecha es válida
            return true; // Fecha válida
        } catch (ParseException e) {
            return false; // Fecha no válida
        }
    }

    // MÉTODO PARA GENERAR EL PDF CON EL INFORME ---------------------------------------------------------------------------------------
    public void generatePdf(List<Libro> libros, String outputPath) {
        try (PDDocument documento = new PDDocument()) {
            // Crea una página A4 en horizontal
            PDPage pagina = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
            documento.addPage(pagina);
            PDPageContentStream contenido = new PDPageContentStream(documento, pagina);

            // Configuración de la tabla
            float margin = 30; // Margen lateral
            float topMargin = 50; // Margen superior
            float bottomMargin = 30; // Margen inferior
            float yStart = pagina.getMediaBox().getHeight() - topMargin;
            float tableWidth = pagina.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;

            // ** Agregar Título "Listado de Libros" ** 
            contenido.beginText();
            contenido.setFont(PDType1Font.COURIER_BOLD, 18); // Fuente y tamaño para el título
            contenido.setLeading(14.5f); // Espacio entre líneas
            contenido.newLineAtOffset(margin, yPosition); // Posiciona el texto en la parte superior de la página
            contenido.showText("LISTADO DE LIBROS"); // Texto del título
            contenido.endText();

            // Ajustar la posición Y después del título
            yPosition -= 40; // Baja la posición para empezar la tabla después del título

            // Definir el encabezado de la tabla
            String[] encabezado = {"Título", "Autor", "Género", "Páginas", "Estado"};
            float[] columnWidth = {250, 200, 170, 60, 100}; // Anchos de columna definidos

            // Dibuja el encabezado
            float startX = margin;
            for (int i = 0; i < encabezado.length; i++) {
                // Dibuja el fondo del encabezado
                contenido.setNonStrokingColor(0, 102, 153); // Color azul del proyecto (RGB)
                contenido.addRect(startX, yPosition, columnWidth[i], 25); // Aumenta la altura
                contenido.fill();

                // Dibuja el texto del encabezado
                contenido.setNonStrokingColor(255, 255, 255); // Color negro (RGB)
                contenido.beginText();
                contenido.setFont(PDType1Font.COURIER_BOLD, 12);

                // Alineación vertical centrada
                float textHeight = 12; // Altura de la fuente
                float textYPosition = yPosition + (25 - textHeight) / 2; // Centrar el texto verticalmente

                contenido.newLineAtOffset(startX + 5, textYPosition); // Margen dentro de la celda
                contenido.showText(encabezado[i]);
                contenido.endText();

                // Actualiza la posición X para la siguiente columna
                startX += columnWidth[i];
            }

            // Baja la posición para las filas
            yPosition -= 25; // Aumenta el espacio para las filas

            for (Libro libro : libros) {
                startX = margin;

                // Evaluar el estado según la lógica
                String estado;

                // Obtener las fechas de inicio y fin
                Date fechaInicio = libro.getFecha_inicio(); // Obtener fecha de inicio
                Date fechaFin = libro.getFecha_fin(); // Obtener fecha de fin

                // Lógica para determinar el estado
                if (fechaInicio == null) {
                    estado = "Pendiente"; // No hay fecha de inicio
                } else if (fechaFin == null) {
                    estado = "Progreso"; // Hay fecha de inicio pero no fin
                } else {
                    estado = "Terminado"; // Hay ambas fechas
                }

                System.out.println("Inicio: " + fechaInicio + ", Fin: " + fechaFin);

                String[] datos = {
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getGenero(),
                    String.valueOf(libro.getNumero_paginas()),
                    estado // Estado calculado
                };

                for (int i = 0; i < datos.length; i++) {
                    // Dibuja cada celda
                    contenido.setNonStrokingColor(255, 255, 255); // Color blanco (RGB)
                    contenido.addRect(startX, yPosition, columnWidth[i], 25); // Aumenta la altura de la celda
                    contenido.fill();

                    // Dibuja el texto dentro de cada celda
                    contenido.setNonStrokingColor(0, 0, 0); // Color negro (RGB)
                    contenido.beginText();
                    contenido.setFont(PDType1Font.COURIER_BOLD, 10);

                    // Alineación vertical centrada para los datos
                    float textHeight = 10; // Altura de la fuente
                    float textYPosition = yPosition + (25 - textHeight) / 2; // Centrar el texto verticalmente

                    contenido.newLineAtOffset(startX + 5, textYPosition); // Margen dentro de la celda
                    contenido.showText(datos[i]);
                    contenido.endText();

                    // Actualiza la posición X para la siguiente columna
                    startX += columnWidth[i];
                }

                // Baja la posición para la siguiente fila
                yPosition -= 25; // Aumenta la altura de la fila
            }

            contenido.close();
            documento.save(outputPath);
            System.out.println("PDF generado en: " + outputPath);
        } catch (Exception e) {
            System.out.println("Error al generar el PDF: " + e.getMessage());
        }
    }

    // MÉTODOS PÚBLICOS PARA MODIFICAR LA VISIBILIDAD DE LOS PANELES Y LOS BOTONES -----------------------------------------------------
    public void mostrarPanelVerTodos() {
        panelVerTodos.setVisible(true);
    }

    public void mostrarPanelAgregar() {
        panelAgregar.setVisible(true);
    }

    public void mostrarPanelEditar() {
        panelEditar.setVisible(true);
    }

    public void ocultarPanelVerTodos() {
        panelVerTodos.setVisible(false);
    }

    public void ocultarPanelAgregar() {
        panelAgregar.setVisible(false);
    }

    public void ocultarPanelEditar() {
        panelEditar.setVisible(false);
    }

    // Métodos para los botones.
    public void desactivarBotonBorrar() {
        btnBorrar.setEnabled(false);
    }
    // ---------------------------------------------------------------------------------------------------------------------------------

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnCargarPortada;
    private javax.swing.JButton btnCargarPortadaEditar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnGuardarAgregar;
    private javax.swing.JButton btnGuardarEditar;
    private javax.swing.JButton btnLimpiarAgregar;
    private javax.swing.JButton btnLimpiarEditar;
    private javax.swing.JButton btnPdf;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnVerTodos;
    private javax.swing.JComboBox<String> cbFormato;
    private javax.swing.JComboBox<String> cbFormatoEditar;
    private javax.swing.JComboBox<String> cbIdioma;
    private javax.swing.JComboBox<String> cbIdiomaEditar;
    private javax.swing.JCheckBox checkEmpezadoHoy;
    private javax.swing.JCheckBox checkTerminadoHoy;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblAutorFavorito;
    private javax.swing.JLabel lblGeneroFavorito;
    private javax.swing.JLabel lblPortada;
    private javax.swing.JLabel lblPortadaEditar;
    private javax.swing.JPanel panelAgregar;
    private javax.swing.JPanel panelAgregar1;
    private javax.swing.JPanel panelAgregar2;
    private javax.swing.JPanel panelAgregar3;
    private javax.swing.JPanel panelAgregar4;
    private javax.swing.JPanel panelAgregar5;
    private javax.swing.JPanel panelAgregarTitulo;
    private javax.swing.JPanel panelBotonPDF;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel panelEditar;
    private javax.swing.JPanel panelEditar1;
    private javax.swing.JPanel panelEditar2;
    private javax.swing.JPanel panelEditar3;
    private javax.swing.JPanel panelEditar4;
    private javax.swing.JPanel panelEditar5;
    private javax.swing.JPanel panelEditarTitulo;
    private javax.swing.JPanel panelPreferidos;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JPanel panelTabla;
    private javax.swing.JPanel panelVerTodos;
    private javax.swing.JTable tblTablaLibros;
    private javax.swing.JTextField txtAutor;
    private javax.swing.JTextField txtAutorEditar;
    private javax.swing.JTextField txtCalificacionEditar;
    private javax.swing.JTextField txtEditorial;
    private javax.swing.JTextField txtEditorialEditar;
    private javax.swing.JTextField txtFechaFinEditar;
    private javax.swing.JTextField txtFechaInicioEditar;
    private javax.swing.JTextField txtFechaPublicacion;
    private javax.swing.JTextField txtFechaPublicacionEditar;
    private javax.swing.JTextField txtGenero;
    private javax.swing.JTextField txtGeneroEditar;
    private javax.swing.JTextField txtISBN;
    private javax.swing.JTextField txtISBNEditar;
    private javax.swing.JTextField txtPaginas;
    private javax.swing.JTextField txtPaginasEditar;
    private javax.swing.JTextField txtSaga;
    private javax.swing.JTextField txtSagaEditar;
    private javax.swing.JTextArea txtSinopsis;
    private javax.swing.JTextArea txtSinopsisEditar;
    private javax.swing.JTextField txtTitulo;
    private javax.swing.JTextField txtTituloEditar;
    // End of variables declaration//GEN-END:variables
}
