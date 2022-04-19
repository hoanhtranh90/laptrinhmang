import { Editor } from '@tinymce/tinymce-react';
import React, { useRef } from 'react';

interface Props {
    setEditor: (editor: React.MutableRefObject<any>) => void,
    valueEditor: string,
    setValueEditor: any,
}

const TinyEditor = ({ setEditor, valueEditor, setValueEditor }: Props) => {
    const editorRef = useRef<any>(null);

    const handleUpdate = (value: string, editor: any) => {
        const length = editor.getContent({ format: 'text' }).length;
        if (length <= 5000) {
            setValueEditor(value);
        } else {
            editor.setContent(valueEditor);
        }
    };

    const handleBeforeAddUndo = (evt: any, editor: any) => {
        const length = editor.getContent({ format: 'text' }).length;
        if (length > 5000) {
            evt.preventDefault();
        }
    };
    return (
        <>
            <Editor
                tinymceScriptSrc="/tinymce/tinymce.min.js"
                onInit={(evt, editor) => {
                    editorRef.current = editor;
                    setEditor(editorRef.current);
                }}
                value={valueEditor}
                onBeforeAddUndo={handleBeforeAddUndo}
                onEditorChange={handleUpdate}
                init={{
                    height: 500,
                    menubar: true,
                    plugins: [
                        'print preview paste importcss searchreplace autolink autosave save',
                        'directionality code visualblocks visualchars fullscreen image link media',
                        'template codesample table charmap hr pagebreak nonbreaking anchor toc insertdatetime',
                        'advlist lists wordcount imagetools textpattern noneditable help charmap quickbars emoticons'
                    ],
                    toolbar: 'undo redo | bold italic underline strikethrough | fontselect fontsizeselect formatselect |' +
                        'alignleft aligncenter alignright alignjustify | outdent indent |  numlist bullist | forecolor backcolor' +
                        'removeformat | pagebreak | charmap emoticons | fullscreen  preview save print | insertfile image media' +
                        'template link anchor codesample | ltr rtl',
                    content_style: 'body { font-family:Times New Roman; font-size:13px }',
                    contextmenu: 'link image imagetools table',
                    images_upload_url: '/',
                }}
            />
        </>)
}

export default TinyEditor;