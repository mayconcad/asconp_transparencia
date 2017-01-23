$(document)
    .ready(function() {
    	
    $("a.ui-panel-titlebar-icon")
        .unbind("click");

    $(".ui-panel-titlebar")
        .live("click", function() {
        var panel = $(this)
            .find(".ui-panel-titlebar-icon span");
        if (panel != null && panel.size() != 0) {
            $(this)
                .parent()
                .find(".ui-panel-content")
                .slideToggle("slow");
            var plus = "ui-icon ui-icon-plusthick";
            var minus = "ui-icon ui-icon-minusthick";
            if (panel.attr("class")
                .indexOf(plus) != -1) {
                panel.attr("class", minus);
            } else {
                panel.attr("class", plus);
            }
        }
    });

    $(".ui-tabs")
        .removeClass("ui-widget-content");
});

PrimeFaces.widget.DataTable.prototype.viewMode = function(cell) {
	var cellEditor = cell.children('div.ui-cell-editor'),
	editableContainer = cellEditor.children('div.ui-cell-editor-input'),
	displayContainer = cellEditor.children('div.ui-cell-editor-output'),

	inputs = editableContainer.find(':input:enabled');
	cell.data('multi-edit', false);
	if(cell.data('multi-edit')) {
		displayContainer.text(cell.data('old-value').join(this.cfg.cellSeparator)).show();
	}
	else {
		var value;
		$input = inputs.eq(0);
		if ($input.is('select')) {
			value = $input.find('option:selected').text();
		} else {
			value = $input.val();
		}
		displayContainer.text(value).show();
	}
	cell.removeClass('ui-cell-editing ui-state-error ui-state-highlight');
	editableContainer.hide();
	cell.removeData('old-value').removeData('multi-edit');
};

PrimeFaces.widget.DataTable.prototype.bindEditEvents = function() {
    var $this = this;
    this.cfg.cellSeparator = this.cfg.cellSeparator||' ';
    
    if(this.cfg.editMode === 'row') {
        var rowEditorSelector = this.jqId + ' tbody.ui-datatable-data > tr > td > div.ui-row-editor';
        
        $(document).off('click.datatable', rowEditorSelector)
                    .on('click.datatable', rowEditorSelector, null, function(e) {
                        var element = $(e.target),
                        row = element.closest('tr');
                        
                        if(element.hasClass('ui-icon-pencil')) {
                            $this.showEditors(row);
                            element.hide().siblings().show();
                        }
                        else if(element.hasClass('ui-icon-check')) {
                            $this.saveRowEdit(row);
                        }
                        else if(element.hasClass('ui-icon-close')) {
                            $this.cancelRowEdit(row);
                        }
                    });
    }
    else if(this.cfg.editMode === 'cell') {
        var cellSelector = this.jqId + ' tbody.ui-datatable-data tr td.ui-editable-column';
        
        // workaround-primefaces edit 'celledit'
        $(cellSelector).each(function() {
        	var value = $(this).find(":input:first").val();
        	if (value != null && value != "") {
        		$this.viewMode($(this));
           }
        }); 
        
        $(document).off('click.datatable-cell', cellSelector)
                    .on('click.datatable-cell', cellSelector, null, function(e) {
                        $this.incellClick = true;
                        
                        var cell = $(this);
                        if(!cell.hasClass('ui-cell-editing')) {
                            $this.showCellEditor($(this));
                        }
                    });
                    
        $(document).off('click.datatable-cell-blur' + this.id)
                    .on('click.datatable-cell-blur' + this.id, function(e) {                            
                        if(!$this.incellClick && $this.currentCell && !$this.contextMenuClick) {
                            $this.saveCell($this.currentCell);
                        }
                        
                        $this.incellClick = false;
                        $this.contextMenuClick = false;
                    });
    }
};

function blockButton(campo) { try {document.getElementById(campo).disabled=true;document.getElementById(campo).firstChild.innerHTML ="Aguarde...";document.getElementById(campo).classList.add('ui-state-disabled');} catch (err) { } }
