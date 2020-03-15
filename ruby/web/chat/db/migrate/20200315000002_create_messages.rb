class CreateMessages < ActiveRecord::Migration[6.0]
  def change
    create_table :messages do |t|
      t.references :chat_room, null: false, foreign_key: true
      t.references :user, null: false, foreign_key: true
      t.string :message, null: false
      t.boolean :disabled, null: false, default: false
      t.datetime :created_at, null: false
    end
  end
end
