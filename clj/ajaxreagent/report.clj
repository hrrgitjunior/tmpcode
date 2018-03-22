(ns ajaxreagent.report
  (:import  [net.sf.dynamicreports.report.builder DynamicReports DatasetBuilder Units SortBuilder]
            [net.sf.dynamicreports.report.builder.column Columns ColumnBuilder TextColumnBuilder]
            [net.sf.dynamicreports.report.builder.datatype DataTypes]
            [net.sf.dynamicreports.report.builder.component Components ComponentBuilder TextFieldBuilder
              DimensionComponentBuilder VerticalListBuilder ImageBuilder]
            [net.sf.dynamicreports.report.datasource DRDataSource]
            [net.sf.dynamicreports.report.constant PageType ComponentPositionType PageOrientation]
            [java.io FileOutputStream]
            [net.sf.dynamicreports.report.builder.style StyleBuilder]
            [net.sf.dynamicreports.report.constant HorizontalAlignment HorizontalImageAlignment
             HorizontalTextAlignment ListType VerticalTextAlignment]
            [net.sf.dynamicreports.report.builder.chart BarChartBuilder]
            [net.sf.dynamicreports.report.builder.crosstab CrosstabBuilder]
            [net.sf.dynamicreports.report.base.expression AbstractSimpleExpression]
            [java.awt Color]
            [net.sf.dynamicreports.report.definition  ReportParameters]
            [java.util ArrayList]))

;
(defprotocol Dateable
  (foo [obj]))

;
(extend-protocol Dateable
  Number
  (foo [d] d))


(defn- component-list [& items]
  (into-array ComponentBuilder items))
;
(defn build-image []
  (let [
        report (DynamicReports/report)
        cmp (DynamicReports/cmp)
        stl (DynamicReports/stl)
        report-file-path (str "resources/public/test_report_3.pdf")
        img-path-src (str "/data/image1.png")
        note-txt
        (-> cmp (.verticalList
                  (into-array ComponentBuilder
                    (map
                      (fn [txt]
                        (-> cmp (.text txt)))
                      [1 2]))))]



    (-> report
      (.title
        (component-list
          (-> cmp (.text (str "Image Report")))))

      (.summary
       (component-list
         (-> cmp (.horizontalList
                   (into-array ComponentBuilder
                     [(-> cmp (.image img-path-src)
                              (.setHorizontalAlignment HorizontalAlignment/CENTER)
                              (.setDimension  (int 200) (int 200)))])))))

     (.pageFooter
       (component-list
         note-txt
         (-> cmp (.text (str "DDDDD")))
         (-> cmp (.text "Footer Band 2"))
         (-> cmp (.text "Footer Band 3")))))))



(defn build-table []
  (let [
        report (DynamicReports/report)
        cmp (DynamicReports/cmp)
        stl (DynamicReports/stl)
        col (DynamicReports/col)
        grid (DynamicReports/grid)
        pen (-> stl (.pen1Point))
        column-style
        (-> stl
          (.style)
          (.setBorder pen)
          (.setHorizontalTextAlignment HorizontalTextAlignment/CENTER));

        title-style
        (-> stl
          (.style)
          (.setBorder pen)
          (.setHorizontalTextAlignment HorizontalTextAlignment/CENTER);
          (.setBackgroundColor (Color. (int 200) (int 200) (int 200))))

        even-style
        (-> stl
          (.simpleStyle)
          (.setBackgroundColor (Color. (int 100) (int 255) (int 255))))



        idColumn  (.column col "Id" "id" java.lang.String)
        nameColumn  (.column col "Name" "name" java.lang.String)
        ds (DRDataSource. (into-array  ["id" "name"]))]

    (.add ds  (into-array  ["1" "Stoan"]))
    (.add ds  (into-array  ["2" "Ivan"]))
    (.add ds  (into-array  ["3" "Boris"]))
    (-> report
      (.title
        (component-list
          (-> cmp (.text (str "Table Report")))))

      (.pageHeader
        (component-list
            (-> cmp (.text "DataSource"))))

      (.setDetailEvenRowStyle even-style)
      (.highlightDetailEvenRows)


      (.columnGrid (into-array TextColumnBuilder [idColumn nameColumn]))

      (.setColumnTitleStyle title-style)

      (.setColumnStyle column-style)

      (.columnGrid ListType/HORIZONTAL_FLOW)


      (.columns (into-array ColumnBuilder
                  [idColumn nameColumn]))

      (.setDataSource ds))))



(defn build-report []
  (let [report (DynamicReports/report)
        cmp (DynamicReports/cmp)
        stl (DynamicReports/stl)
        image-report (build-image)
        table-report (build-table)
        report-file-path "resources/public/test_report_3.pdf"
        conc-report (DynamicReports/concatenatedReport)
        report1 (DynamicReports/report)
        report2 (DynamicReports/report)]


    (println "#####" conc-report)
    (-> conc-report
        (.concatenate (into-array [image-report table-report]))
    ;   ; (build-image)
    ;   ; (build-table)
       (.toPdf (FileOutputStream. report-file-path)))
    {:data "AAA"}))
